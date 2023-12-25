package co.id.Asset.eInvoice.Controller;

import co.id.Asset.eInvoice.Service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("invoice")
public class InvoiceController {
   @Autowired
   private InvoiceService invoiceService;

   @GetMapping("/detail")
   public String getInvoice(@RequestParam(defaultValue = "") String invoiceNumber){
           return "Invoice/detail-invoice";
   }

}
