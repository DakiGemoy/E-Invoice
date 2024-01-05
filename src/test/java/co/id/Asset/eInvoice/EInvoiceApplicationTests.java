package co.id.Asset.eInvoice;

import co.id.Asset.eInvoice.Service.ClientService;
import co.id.Asset.eInvoice.Service.ExcelService;
import co.id.Asset.eInvoice.Service.InvoiceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;

@SpringBootTest
class EInvoiceApplicationTests {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ClientService clientService;

    @Test
    void testExcel(){
        excelService.test();
    }

    @Test
    void getInvoice() throws IOException, InvalidFormatException {
        invoiceService.sendToExcel(LocalDate.of(2023,12,20),
                LocalDate.of(2023,12,31));
    }

}
