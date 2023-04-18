package mx.lania.g4d.service.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import mx.lania.g4d.domain.*;
import mx.lania.g4d.service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelUploadService {

    private UserService userService;
    private IteracionService iteracionService;
    private FuncionalidadService funcionalidadService;
    private AtributoFuncionalidadService atributoFuncionalidadService;
    private AtributoService atributoService;

    private ProyectoService proyectoService;

    ExcelUploadService(
        UserService userService,
        IteracionService iteracionService,
        FuncionalidadService funcionalidadService,
        AtributoFuncionalidadService atributoFuncionalidadService,
        AtributoService atributoService,
        ProyectoService proyectoService
    ) {
        this.userService = userService;
        this.iteracionService = iteracionService;
        this.funcionalidadService = funcionalidadService;
        this.atributoFuncionalidadService = atributoFuncionalidadService;
        this.atributoService = atributoService;
        this.proyectoService = proyectoService;
    }

    public boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public List<Funcionalidad> getFuncionalidadsDataFromExcel(InputStream inputStream, Long proyectoId) {
        List<Funcionalidad> funcionalidads = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("CARGA INICIAL");
            int rowIndex = 0;
            List<Atributo> atributosExtra = new ArrayList<>();
            for (Row row : sheet) {
                if (rowIndex < 1) {
                    rowIndex++;
                    continue;
                }

                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;

                Funcionalidad funcionalidad = new Funcionalidad();
                Set<AtributoFuncionalidad> atributoFuncionalidads = new HashSet<>();

                if (rowIndex == 1) {
                    int cIndex = 0;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        if (cIndex >= 7) {
                            if (!cell.getStringCellValue().isEmpty()) {
                                Optional<Atributo> atributo = atributoService.findOneByNombre(cell.getStringCellValue());
                                if (atributo.isPresent()) {
                                    atributosExtra.add(atributo.get());
                                } else {
                                    Atributo nuevoAtributo = new Atributo();
                                    nuevoAtributo.setNombre(cell.getStringCellValue());
                                    nuevoAtributo.setParaGitLab(false);
                                    Atributo nuevoAtributoG = atributoService.save(nuevoAtributo);
                                    atributosExtra.add(nuevoAtributoG);
                                }
                            }
                        }
                        cIndex++;
                    }
                    rowIndex++;
                    System.out.println(atributosExtra);
                    continue;
                }
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0:
                            // funcionalidad.setId(Double.doubleToLongBits(cell.getNumericCellValue()));
                            break;
                        case 1:
                            funcionalidad.setNombre(cell.getStringCellValue());
                            break;
                        case 2:
                            funcionalidad.setDescripcion(cell.getStringCellValue());
                            break;
                        case 3:
                            String usuariosRaw = cell.getStringCellValue();
                            String[] subcadenas = usuariosRaw.split(",");
                            Set<User> userSet = new HashSet<>();
                            for (String sub : subcadenas) {
                                Optional<User> userOptional = userService.findByLogin(sub.trim());
                                if (userOptional.isPresent()) {
                                    userSet.add(userOptional.get());
                                }
                            }
                            funcionalidad.setUsers(userSet);
                            break;
                        case 4:
                            if (cell.getStringCellValue().isEmpty()) {
                                break;
                            }
                            Optional<Iteracion> iteracion = iteracionService.findOneByNombreAndProyectoId(
                                cell.getStringCellValue(),
                                proyectoId
                            );
                            if (iteracion.isPresent()) {
                                funcionalidad.setIteracion(iteracion.get());
                            } else {
                                Iteracion iteracionNueva = new Iteracion();
                                Optional<Proyecto> p = proyectoService.findOne(proyectoId);
                                if (p.isPresent()) {
                                    iteracionNueva.setProyecto(p.get());
                                    iteracionNueva.setNombre(cell.getStringCellValue());
                                    funcionalidad.setIteracion(iteracionService.save(iteracionNueva));
                                }
                            }
                            break;
                        case 5:
                            funcionalidad.setPrioridad(cell.getStringCellValue());
                            break;
                        case 6:
                            funcionalidad.setEstatusFuncionalidad(cell.getStringCellValue());
                        default:
                            // cell index 7-7 = 0 ; 8-7 = 1
                            // 0,1
                            // [Puntos 7, Talla]
                            if (cellIndex - 7 < 0) {
                                break;
                            }

                            if (atributosExtra.get(cellIndex - 7) != null) {
                                String celda;
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    celda = Double.toString(cell.getNumericCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                    celda = cell.getStringCellValue();
                                } else {
                                    break;
                                }

                                AtributoFuncionalidad preAtributo = new AtributoFuncionalidad();
                                preAtributo.setValor(celda);
                                preAtributo.setMarcado(false);
                                preAtributo.setAtributo(atributosExtra.get(cellIndex - 7));

                                atributoFuncionalidads.add(preAtributo);
                            }

                            break;
                    }
                    // se agreaga todas los atributos adicionales

                    cellIndex++;
                }
                //System.out.println(atributoFuncionalidads );

                //crear y retornar la funcionalidad creada
                Funcionalidad nuevaFuncionalidad = funcionalidadService.save(funcionalidad);

                for (AtributoFuncionalidad a : atributoFuncionalidads) {
                    a.setFuncionalidad(nuevaFuncionalidad);
                    atributoFuncionalidadService.save(a);
                }

                atributoFuncionalidads.clear();
                System.out.println(funcionalidad);

                funcionalidads.add(nuevaFuncionalidad);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return funcionalidads;
    }

    public byte[] generarExcel(Long id) throws IOException {
        List<Funcionalidad> funcionalidadList = funcionalidadService.findAllByProyectoId(id);
        System.out.println(funcionalidadList);

        Workbook workbook = new XSSFWorkbook(); // crea un libro de trabajo de Excel
        Sheet sheet = workbook.createSheet("Mi hoja"); // crea una hoja en el libro de trabajo

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0); // crea la fila de encabezado
        Cell idCell = headerRow.createCell(0);
        idCell.setCellValue("id (no modificar)");

        idCell.setCellStyle(headerStyle);
        headerRow.createCell(1).setCellValue("Nombre*");
        headerRow.createCell(2).setCellValue("Descripción*");
        headerRow.createCell(3).setCellValue("Asignación");
        headerRow.createCell(4).setCellValue("Iteración");
        headerRow.createCell(5).setCellValue("prioridad");
        headerRow.createCell(6).setCellValue("Estatus");

        int index = 7;

        //iterar iteraciones extras
        List<Atributo> atributoList = atributoService.findAll();
        for (Atributo atributo : atributoList) {
            headerRow.createCell(index).setCellValue(atributo.getNombre());
            index++;
        }

        int indexRow = 1;
        for (Funcionalidad f : funcionalidadList) {
            Row dataRow = sheet.createRow(indexRow); // crea una fila de datos
            dataRow.createCell(0).setCellValue(f.getId());
            dataRow.createCell(1).setCellValue(f.getNombre());
            dataRow.createCell(2).setCellValue(f.getDescripcion());
            //iterar users
            String userArray = "";
            for (User user : f.getUsers()) {
                userArray = userArray.concat(user.getLogin() + ", ");
            }
            userArray = userArray.substring(0, userArray.length() - 2);

            dataRow.createCell(3).setCellValue(userArray);
            dataRow.createCell(4).setCellValue(f.getIteracion().getNombre());
            dataRow.createCell(5).setCellValue(f.getPrioridad());
            dataRow.createCell(6).setCellValue(f.getEstatusFuncionalidad());

            index = 7;
            for (Atributo atributo : atributoList) {
                boolean flag = false;
                for (AtributoFuncionalidad atributoFuncionalidad : f.getAtributoFuncionalidads()) {
                    if (atributoFuncionalidad.getAtributo().getNombre().equals(atributo.getNombre())) {
                        dataRow.createCell(index).setCellValue(atributoFuncionalidad.getValor());
                        flag = true;
                    }
                }
                if (!flag) {
                    dataRow.createCell(index).setCellValue("");
                }
                index++;
            }

            indexRow++;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream); // escribe el libro de trabajo en un flujo de salida de bytes
        workbook.close(); // cierra el libro de trabajo
        return outputStream.toByteArray(); // devuelve los bytes del libro de trabajo como un arreglo de bytes
    }
}
