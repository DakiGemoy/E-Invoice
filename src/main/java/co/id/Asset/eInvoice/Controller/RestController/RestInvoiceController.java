package co.id.Asset.eInvoice.Controller.RestController;

import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.CarRequest;
import co.id.Asset.eInvoice.Model.InvoicePaging;
import co.id.Asset.eInvoice.Model.InvoiceUpsert;
import co.id.Asset.eInvoice.Service.InvoiceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/invoice")
public class RestInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/generate")
    public BaseResponse generateInvoiceNumber(@RequestParam Integer dsoId){
        return invoiceService.invoiceNumberGenerator(dsoId);
    }

    @PostMapping("/save-invoice")
    public BaseResponse createInvoice(@RequestBody InvoiceUpsert payload){
        return invoiceService.saveInvoice(payload);
    }

    @GetMapping("/get-invoice")
    public BaseResponse getInvoice(@RequestParam(defaultValue = "") String invoiceNumber){
        return invoiceService.getInvoice(invoiceNumber);
    }

    @PostMapping("/add-car")
    public BaseResponse addCarToInvoice(@RequestBody CarRequest carRequest){
        return invoiceService.saveCarToInvoice(carRequest);
    }

    @GetMapping("/check")
    public Boolean checkInvoice(@RequestParam String invoiceNumber){
        return invoiceService.checkInvoiceNumber(invoiceNumber);
    }

    @GetMapping("/get-desc")
    public BaseResponse getDescription(@RequestParam Integer descId){
        return invoiceService.getDescById(descId);
    }

    @DeleteMapping("/delete-desc")
    public BaseResponse deleteDescription(@RequestParam Integer descId){
        return invoiceService.deleteDescription(descId);
    }

    @GetMapping("/reminder")
    public BaseResponse reminderInvoice(@RequestParam String invoiceNumber){
        return invoiceService.reminderClient(invoiceNumber);
    }

//    public BaseResponse listInvoice(@RequestParam(defaultValue = "") String search,
//                                    @RequestParam(defaultValue = "1") Integer page,
//                                    @RequestParam(defaultValue = "") String rangeFrom,
//                                    @RequestParam(defaultValue = "") String rangeTo)
    @PostMapping("/list")
    public BaseResponse listInvoice(@RequestBody InvoicePaging paging)
    {
        return invoiceService.getPagingInvoice(paging);
    }

    @DeleteMapping("/delete-invoice")
    public BaseResponse deleteInvoice(@RequestParam String invoiceNumber){
        return invoiceService.deleteInvoice(invoiceNumber);
    }

    @GetMapping("/sendExcel")
    public BaseResponse sendToExcel(@RequestParam String rangeFrom,
                                    @RequestParam String rangeTo) throws IOException, InvalidFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return invoiceService.sendToExcel(
                LocalDate.parse(rangeFrom, formatter),
                LocalDate.parse(rangeTo,formatter));
    }

}
