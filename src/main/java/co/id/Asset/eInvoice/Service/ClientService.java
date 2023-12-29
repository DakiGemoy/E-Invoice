package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Entity.Client;
import co.id.Asset.eInvoice.Database.Repository.ClientRepository;
import co.id.Asset.eInvoice.Database.Repository.DsoClientRepository;
import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.ClientInput;
import co.id.Asset.eInvoice.Model.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DsoClientRepository dsoClientRepository;

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

    public BaseResponse getClientDso(Integer dsoId){
        var dso = dsoClientRepository.findById(dsoId)
                .orElseThrow(()-> new EntityNotFoundException("DSO not found : "+dsoId));

        return new BaseResponse(200,"Success",null, dso.getClientCode());
    }
}
