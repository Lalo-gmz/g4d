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

    private UserService userService;
    private IteracionService iteracionService;

    ExcelUploadService(UserService userService, IteracionService iteracionService) {
        this.userService = userService;
        this.iteracionService = iteracionService;
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
                            break;
                        case 5:
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
                        case 6:
                            funcionalidad.setEstatusFuncionalidad(cell.getStringCellValue());
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
                            funcionalidad.setPrioridad(cell.getStringCellValue());
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
