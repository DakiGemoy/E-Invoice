package co.id.Asset.eInvoice.Controller.RestController;

import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api//invoice")
public class RestInvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/create")
    public BaseResponse createInvoice(@RequestParam String clientCode){
        return invoiceService.createInvoice(clientCode);
    }

    @GetMapping("/list")
    public BaseResponse getPaginationInvoice(@RequestParam(defaultValue = "") String invoiceNumber,
                                             @RequestParam(defaultValue = "") String spkNumber,
                                             @RequestParam(defaultValue = "1") String page ){
        return invoiceService.getPagingInvoice(invoiceNumber,spkNumber,Integer.valueOf(page));
    }
}
