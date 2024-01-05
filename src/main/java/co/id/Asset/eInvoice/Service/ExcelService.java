package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Repository.InvoiceRepository;
import co.id.Asset.eInvoice.Model.InvoiceForExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Value("${excel.file.location}")
    private String excelLocation;

    private final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    public void test(){
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelLocation));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(1);

            Object[][] bookData = {
                    {"2023-10-23", "Test keterangan", "test plat nomor","test spk","test nama perusahaan","invoice number",1000},
                    {"2023-10-23", "Test keterangan1", "test plat nomor1","test spk1","test nama perusahaan1","invoice number1",1001},
            };

            int rowCount = sheet.getLastRowNum();

            for (Object[] aBook : bookData) {
                Row row = sheet.createRow(++rowCount);

                int columnCount = 0;

                Cell cell = row.createCell(columnCount);
                cell.setCellValue(rowCount);

                for (Object field : aBook) {
                    cell = row.createCell(++columnCount);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                }

            }

            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream("excelUpdate.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToExcel(List<InvoiceForExcel> invoices) throws IOException, InvalidFormatException {
        logger.info("Start sent excel");
        ObjectMapper mapper = new ObjectMapper();

        try {
            FileInputStream inputStream = new FileInputStream(new File(excelLocation));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeight(9);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setWrapText(true);
            normalStyle.setFont(font);
            normalStyle.setAlignment(HorizontalAlignment.CENTER);
            normalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            normalStyle.setBorderRight(BorderStyle.THIN);
            normalStyle.setBorderLeft(BorderStyle.THIN);
            normalStyle.setBorderTop(BorderStyle.THIN);
            normalStyle.setBorderBottom(BorderStyle.THIN);

            CreationHelper creationHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            dateStyle.setFont(font);
            dateStyle.setAlignment(HorizontalAlignment.CENTER);
            dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dateStyle.setBorderRight(BorderStyle.THIN);
            dateStyle.setBorderLeft(BorderStyle.THIN);
            dateStyle.setBorderTop(BorderStyle.THIN);
            dateStyle.setBorderBottom(BorderStyle.THIN);

            for(var i : invoices){
                Map<String, Object> map = mapper.convertValue(i, Map.class);
                Row row = sheet.createRow(++rowCount);

                int columnCount = 0;

                Cell cell = row.createCell(columnCount);
                cell.setCellValue(rowCount);
                cell.setCellStyle(normalStyle);

                var fields = i.getClass().getDeclaredFields();

                for(var f : fields){
                    cell = row.createCell(++columnCount);

                    switch (f.getType().getSimpleName()){
                        case "Integer":
                            cell.setCellValue((Integer) map.get(f.getName()));
                            cell.setCellStyle(normalStyle);
                            cell.setCellType(CellType.NUMERIC);
                            break;
                        case "Long":
                        case "long":
                        case "Double":
                            var test = (double) ((long) map.get(f.getName()));
                            cell.setCellValue((double) test);
                            cell.setCellStyle(normalStyle);
                            cell.setCellType(CellType.NUMERIC);
                            break;
                        default:
                            cell.setCellValue(map.get(f.getName()).toString());
                            cell.setCellStyle(normalStyle);
                            cell.setCellType(CellType.STRING);
                    }
                }

                var invoice = invoiceRepository.findById(i.getInvoiceNo()).get();
                invoice.setSendToExcel(true);

                invoiceRepository.save(invoice);
                logger.info(invoice.getInvoiceNumber()+" : Success send to excel..");
            }

            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(excelLocation);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            logger.info("Finish send to excel...");
        } catch (Exception e){
            logger.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
