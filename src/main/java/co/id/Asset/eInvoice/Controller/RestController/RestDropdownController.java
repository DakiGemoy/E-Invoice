package co.id.Asset.eInvoice.Controller.RestController;

import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Service.DropdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dropdown")
public class RestDropdownController {

    @Autowired
    private DropdownService dropdownService;

    @GetMapping("/client")
    public BaseResponse getDropdownClient(){
        return dropdownService.dropdownClient();
    }

    @GetMapping("/dsoClient")
    public BaseResponse getDropdownDso(@RequestParam String clientCode){
        return dropdownService.dropdownDso(clientCode);
    }

    @GetMapping("/region")
    public BaseResponse getDropdownRegion(){
        return dropdownService.dropdownRegion();
    }

    @GetMapping("/vehicle")
    public BaseResponse getDropdownVehicle(@RequestParam(defaultValue = "") String search){
        return dropdownService.dropdownVehicle(search);
    }
}
