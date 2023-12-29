package co.id.Asset.eInvoice.Controller;

import co.id.Asset.eInvoice.Service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("home")
public class HomeController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/page")
    public String homepage(){
        return "Home";
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "1") Integer page,
                            Model model){
        model.addAttribute("list", invoiceService.getInvoicePagingModel(search,page));
        model.addAttribute("totalPage", invoiceService.countTotalPageInvoicePagingModel(search));
        model.addAttribute("currentPage",page);
        return "Menu/dashboard_menu";
    }

}
