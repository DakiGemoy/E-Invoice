package co.id.Asset.eInvoice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("login")
@Controller
public class LoginController {

    @GetMapping("/page")
    public String loginPage(){
        return "Login/loginPage";
    }

}
