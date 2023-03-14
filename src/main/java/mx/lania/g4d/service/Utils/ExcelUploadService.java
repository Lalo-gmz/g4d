package mx.lania.g4d.service.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import mx.lania.g4d.domain.*;
import mx.lania.g4d.service.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelUploadService {

    private EstatusFuncionalidadService estatusFuncionalidadService;
    private UserService userService;
    private IteracionService iteracionService;

    private PrioridadService prioridadService;
    private EtiquetaService etiquetaService;

    ExcelUploadService(
        EstatusFuncionalidadService estatusFuncionalidadService,
        UserService userService,
        IteracionService iteracionService,
        PrioridadService prioridadService,
        EtiquetaService etiquetaService
    ) {
        this.userService = userService;
        this.estatusFuncionalidadService = estatusFuncionalidadService;
        this.iteracionService = iteracionService;
        this.prioridadService = prioridadService;
        this.etiquetaService = etiquetaService;
    }

    public boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public List<Funcionalidad> getFuncionalidadsDataFromExcel(InputStream inputStream, Long proyectoId) {
        List<Funcionalidad> funcionalidads = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Hoja1");
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex < 1) {
                    rowIndex++;
                    continue;
                }

                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;

                Funcionalidad funcionalidad = new Funcionalidad();

                if (rowIndex == 1) {
                    List<String> atributosExtra = new ArrayList<>();
                    int cIndex = 0;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        if (cIndex >= 10) {
                            atributosExtra.add(cell.getStringCellValue());
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
                        //case 0 -> customer.setCustomerId((int) cell.getNumericCellValue());
                        case 1:
                            funcionalidad.setNombre(cell.getStringCellValue());
                            break;
                        case 2:
                            funcionalidad.setDescripcion(cell.getStringCellValue());
                            break;
                        case 3:
                            funcionalidad.setUrlGitLab(cell.getStringCellValue());
                            break;
                        case 4:
                            funcionalidad.setFechaEntrega(LocalDate.from(cell.getLocalDateTimeCellValue()));
                            break;
                        case 5:
                            String login = cell.getStringCellValue();
                            System.out.println(login);
                            Optional<User> optionalUser = userService.findByLogin(login);
                            Set<User> userSet = new HashSet<>();
                            if (optionalUser.isPresent()) {
                                System.out.println(optionalUser.get());
                                userSet.add(optionalUser.get());
                                funcionalidad.setUsers(userSet);
                            }
                            break;
                        case 6:
                            Optional<EstatusFuncionalidad> estatusFuncionalidad = estatusFuncionalidadService.findOneByNombre(
                                cell.getStringCellValue()
                            );
                            if (estatusFuncionalidad.isPresent()) {
                                funcionalidad.setEstatusFuncionalidad(estatusFuncionalidad.get());
                            }
                            break;
                        case 7:
                            Optional<Iteracion> iteracion = iteracionService.findOneByNombreAndProyectoId(
                                cell.getStringCellValue(),
                                proyectoId
                            );
                            if (iteracion.isPresent()) {
                                funcionalidad.setIteracion(iteracion.get());
                            }
                            break;
                        case 8:
                            Optional<Prioridad> prioridadOptional = prioridadService.findOneByNombre(cell.getStringCellValue());
                            if (prioridadOptional.isPresent()) {
                                funcionalidad.setPrioridad(prioridadOptional.get());
                            }
                            break;
                        case 9:
                            String etiquetasRaw = cell.getStringCellValue();

                            String[] subcadenas = etiquetasRaw.split(",");
                            System.out.println(subcadenas);
                            Set<Etiqueta> etiquetaSet = new HashSet<>();
                            for (String sub : subcadenas) {
                                Optional<Etiqueta> etiquetaOptional = etiquetaService.findOneByNombre(sub.trim());
                                if (etiquetaOptional.isPresent()) {
                                    etiquetaSet.add(etiquetaOptional.get());
                                }
                            }
                            funcionalidad.setEtiquetas(etiquetaSet);
                            break;
                        default:
                            break;
                    }

                    cellIndex++;
                }
                funcionalidads.add(funcionalidad);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return funcionalidads;
    }
}
