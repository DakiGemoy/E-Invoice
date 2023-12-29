package co.id.Asset.eInvoice;

import co.id.Asset.eInvoice.Service.ExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EInvoiceApplicationTests {

    @Autowired
    private ExcelService excelService;

    @Test
    private void coba(){
        System.out.println("test");
        excelService.test();
    }

}
