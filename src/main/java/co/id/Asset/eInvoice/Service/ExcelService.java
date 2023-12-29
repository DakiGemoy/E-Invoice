package co.id.Asset.eInvoice.Service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ExcelService {
    @Value("${excel.file.location}")
    private String excelLocation;

    public void test(){
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelLocation));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

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

            FileOutputStream outputStream = new FileOutputStream("excelUpdate.xls");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
