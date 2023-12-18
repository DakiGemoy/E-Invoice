package co.id.Asset.eInvoice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class HomeController {
    @GetMapping("/page")
    public String homepage(){
        return "Home";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "Menu/dashboard_menu";
    }

}
