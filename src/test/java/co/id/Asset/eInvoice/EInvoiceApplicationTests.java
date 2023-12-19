package co.id.Asset.eInvoice;

import co.id.Asset.eInvoice.Service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EInvoiceApplicationTests {

	@Autowired
	private InvoiceService invoiceService;

	@Test
	void testGenerator() {
		String code = "A01";

		invoiceService.invoiceNumberGenerator(code);
	}

}
