package co.id.Asset.eInvoice.Controller.RestController;

import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.InvoiceUpsert;
import co.id.Asset.eInvoice.Service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/invoice")
public class RestInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/generate")
    public BaseResponse generateInvoiceNumber(@RequestParam String clientCode){
        return invoiceService.invoiceNumberGenerator(clientCode);
    }

    @GetMapping("/list")
    public BaseResponse getPaginationInvoice(@RequestParam(defaultValue = "") String invoiceNumber,
                                             @RequestParam(defaultValue = "") String spkNumber,
                                             @RequestParam(defaultValue = "1") String page ){
        return invoiceService.getPagingInvoice(invoiceNumber,spkNumber,Integer.valueOf(page));
    }

    @PostMapping("/save-invoice")
    public BaseResponse createInvoice(@RequestBody InvoiceUpsert payload){
        return invoiceService.saveInvoice(payload);
    }

    @GetMapping("/get-invoice/{invoiceNo}")
    public BaseResponse getInvoice(@PathVariable String invoiceNo){
        return invoiceService.getInvoice(invoiceNo);
    }
}
