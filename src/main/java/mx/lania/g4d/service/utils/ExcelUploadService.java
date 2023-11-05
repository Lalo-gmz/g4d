package mx.lania.g4d.service.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;
import mx.lania.g4d.domain.*;
import mx.lania.g4d.service.*;
import mx.lania.g4d.service.dto.UserDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public List<Funcionalidad> updateFuncionalidadByExcel(InputStream inputStream, Long proyectoId) {
        List<Funcionalidad> funcionalidads = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();

            List<Atributo> atributosExtra = new ArrayList<>();
            List<Funcionalidad> funcionalidadList = funcionalidadService.findAll();
            List<Atributo> atributoList = atributoService.findAll();
            List<UserDTO> userDtoList = userService.findAll();

            for (int i = 0; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                Set<AtributoFuncionalidad> atributoFuncionalidads = new HashSet<>();

                int lastCellNum = row.getLastCellNum();

                List<String> atributosExtrasList = new ArrayList<>();
                List<Atributo> atributosNuevos = new ArrayList<>();

                if (i == 0) {
                    for (int j = 7; j < lastCellNum; j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            atributosExtrasList.add(cell.getStringCellValue());
                        }
                    }

                    boolean encontrado = false;
                    for (String atriString : atributosExtrasList) {
                        for (Atributo atributo : atributoList) {
                            if (atributo.getNombre().equalsIgnoreCase(atriString)) {
                                atributosExtra.add(atributo);
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            Atributo nuevoAtributo = new Atributo();
                            nuevoAtributo.setNombre(atriString);
                            nuevoAtributo.setParaGitLab(false);
                            atributosNuevos.add(nuevoAtributo);
                        }
                    }
                    atributosExtra.addAll(atributoService.saveAll(atributosNuevos));
                }

                Funcionalidad funcionalidad = new Funcionalidad();
                boolean isNewFuncionalidad = true;
                for (int j = 0; j < lastCellNum; j++) {
                    XSSFCell cell = row.getCell(j);

                    if (cell != null) {
                        switch (j) {
                            case 0:
                                if (cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() != 0) {
                                    Optional<Funcionalidad> funcionalidadOptional = funcionalidadList
                                        .stream()
                                        .filter(f -> f.getId().toString().equalsIgnoreCase(cell.getStringCellValue()))
                                        .findFirst();
                                    funcionalidad = funcionalidadOptional.orElseGet(Funcionalidad::new);
                                }
                                break;
                            case 1:
                                funcionalidad.setNombre(cell.getStringCellValue());
                                break;
                            case 2:
                                funcionalidad.setDescripcion(cell.getStringCellValue());
                                break;
                            case 3:
                                String usuariosRaw = cell.getStringCellValue();
                                if (usuariosRaw.isEmpty()) {
                                    funcionalidad.setUsers(new HashSet<>());
                                } else {
                                    String[] subcadenas = usuariosRaw.split(",");
                                    Set<User> userSet = new HashSet<>();
                                    boolean encontrado = false;
                                    for (String sub : subcadenas) {
                                        Optional<User> userOptional = userService.findByLogin(sub.trim());
                                        userOptional.ifPresent(userSet::add);
                                    }
                                    funcionalidad.setUsers(userSet);
                                }

                                break;
                            case 4:
                                String nombreIteracion = cell.getStringCellValue().isEmpty() ? "Por Asignar" : cell.getStringCellValue();
                                Optional<Iteracion> iteracion = iteracionService.findOneByNombreAndProyectoId(nombreIteracion, proyectoId);
                                if (iteracion.isEmpty()) {
                                    Iteracion iteracionNueva = new Iteracion();
                                    Optional<Proyecto> p = proyectoService.findOne(proyectoId);
                                    if (p.isPresent()) {
                                        iteracionNueva.setProyecto(p.get());
                                        iteracionNueva.setNombre(nombreIteracion);
                                        iteracion = Optional.of(iteracionService.save(iteracionNueva));
                                    }
                                }
                                if (iteracion.isPresent()) {
                                    funcionalidad.setIteracion(iteracion.get());
                                }
                                break;
                            case 5:
                                funcionalidad.setPrioridad(cell.getStringCellValue());
                                break;
                            case 6:
                                funcionalidad.setEstatusFuncionalidad(cell.getStringCellValue());
                                break;
                            default:
                                if (cell.getCellType() != CellType.BLANK) {
                                    String celda;
                                    if (cell.getCellType() == CellType.NUMERIC) {
                                        celda = Double.toString(cell.getNumericCellValue());
                                    } else {
                                        celda = cell.getStringCellValue();
                                    }

                                    AtributoFuncionalidad preAtributo = new AtributoFuncionalidad();
                                    preAtributo.setValor(celda);
                                    preAtributo.setMarcado(false);
                                    preAtributo.setAtributo(atributosExtra.get(j - 7));
                                    atributoFuncionalidads.add(preAtributo);
                                }
                                break;
                        }
                    }
                }

                if (isNewFuncionalidad) {
                    funcionalidad = funcionalidadService.save(funcionalidad);
                } else {
                    atributoFuncionalidadService.deleteByFuncionalidad(funcionalidad);
                    funcionalidad.setAtributoFuncionalidads(new HashSet<>());
                }

                Set<AtributoFuncionalidad> nuevosAtributosFuncionalidads = new HashSet<>();

                for (AtributoFuncionalidad a : atributoFuncionalidads) {
                    a.setFuncionalidad(funcionalidad);
                    nuevosAtributosFuncionalidads.add(atributoFuncionalidadService.save(a));
                }

                if (!atributoFuncionalidads.isEmpty()) {
                    funcionalidad.setAtributoFuncionalidads(nuevosAtributosFuncionalidads);
                }

                atributoFuncionalidads.clear();

                funcionalidads.add(funcionalidad);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

        return funcionalidads;
    }

    public byte[] generarExcel(Long id, boolean isPlantilla) throws IOException {
        List<Funcionalidad> funcionalidadList = funcionalidadService.findAllByProyectoId(id);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mi hoja");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            Cell idCell = headerRow.createCell(0);

            idCell.setCellValue(isPlantilla ? "id(Dejar en blanco)" : "id (no modificar)");

            idCell.setCellStyle(headerStyle);
            headerRow.createCell(1).setCellValue("Nombre*");
            headerRow.createCell(2).setCellValue("Descripción*");
            headerRow.createCell(3).setCellValue("Asignación");
            headerRow.createCell(4).setCellValue("Iteración");
            headerRow.createCell(5).setCellValue("prioridad");
            headerRow.createCell(6).setCellValue("Estatus");

            int index = 7;

            List<Atributo> atributoList = atributoService.findAll();
            for (Atributo atributo : atributoList) {
                headerRow.createCell(index).setCellValue(atributo.getNombre());
                index++;
            }

            if (!isPlantilla) {
                int indexRow = 1;
                for (Funcionalidad f : funcionalidadList) {
                    Row dataRow = sheet.createRow(indexRow);
                    dataRow.createCell(0).setCellValue(f.getId());
                    dataRow.createCell(1).setCellValue(f.getNombre());
                    dataRow.createCell(2).setCellValue(f.getDescripcion());
                    String userArray = "";
                    for (User user : f.getUsers()) {
                        userArray = userArray.concat(user.getLogin() + ", ");
                    }
                    if (!userArray.isEmpty()) {
                        userArray = userArray.substring(0, userArray.length() - 2);
                    }

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
                            dataRow.createCell(index).setBlank();
                        }
                        index++;
                    }

                    indexRow++;
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
