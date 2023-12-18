package co.id.Asset.eInvoice.Controller;

import co.id.Asset.eInvoice.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AccountController {
    @Autowired
    private AuthService authService;

    @GetMapping("/list")
    public String getListAuth(@RequestParam(defaultValue = "") String address,
                              Model model){
        model.addAttribute("data",authService.test(address));
        return "Menu/authentication_menu";
    }
}
