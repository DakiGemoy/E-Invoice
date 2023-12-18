package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Entity.Client;
import co.id.Asset.eInvoice.Database.Repository.ClientRepository;
import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.ClientInput;
import co.id.Asset.eInvoice.Model.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public BaseResponse saveClientInsert(ClientInput payload){
        Client client = null;

        if(clientRepository.existsByClientCode(payload.getCode())){
            return new BaseResponse(400,"BAD REQUEST","Client is already exists",null);
        } else {
            client = new Client(payload.getCode(), payload.getName(), payload.getAddress(),payload.getRegionId(), payload.getEmail(), payload.getPhone());
        }

        try {
            clientRepository.save(client);
            return new BaseResponse(200,"Succes save client",null,null);
        } catch (Exception e){
            return new BaseResponse(500, "INTERNAL SERVER ERROR","e.getMessage()",null);
        }
    }

    public BaseResponse getClient(String clientCode){
        var c = clientRepository.findById(clientCode);
        if(c.isEmpty()){
            return new BaseResponse(400,"Data not found",null,null);
        }  else {
            return new BaseResponse(200,"Success get data",null,c.get());
        }
    }

    public BaseResponse listClient(String param, Integer page){
        Integer limit = 3;
        Integer setFetch = 0;
        if(page != 1){
            setFetch = (page-1) * limit ;
        }

        var list = clientRepository.getListClientPagination(param,setFetch,limit);
        var totalData = (double) clientRepository.countData(param);
        Long totalPage = (long) Math.ceil(totalData / limit);
        PaginationResponse response = new PaginationResponse(list,page,totalPage);

        return new BaseResponse(200, null, null, response);
    }
}
