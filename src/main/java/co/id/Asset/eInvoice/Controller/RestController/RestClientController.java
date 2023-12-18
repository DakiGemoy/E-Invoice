package co.id.Asset.eInvoice.Controller.RestController;

import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.ClientInput;
import co.id.Asset.eInvoice.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/client")
public class RestClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/insert")
    public BaseResponse saveClient(@RequestBody ClientInput payload){
        return clientService.saveClientInsert(payload);
    }

    @GetMapping("/getupdate/{clientCode}")
    public BaseResponse getClient(@PathVariable String clientCode){
        return clientService.getClient(clientCode);
    }

    @GetMapping("/getList")
    public BaseResponse getListClient(@RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "1") String page){
        return clientService.listClient(search,Integer.valueOf(page));
    }
}
