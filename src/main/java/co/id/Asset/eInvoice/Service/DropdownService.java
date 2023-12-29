package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Repository.ClientRepository;
import co.id.Asset.eInvoice.Database.Repository.DsoClientRepository;
import co.id.Asset.eInvoice.Database.Repository.MasterRegionRepository;
import co.id.Asset.eInvoice.Database.Repository.VehicleRepository;
import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.DropdownModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DropdownService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MasterRegionRepository masterRegionRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DsoClientRepository dsoClientRepository;

    public BaseResponse dropdownClient(){
        List<DropdownModel> dropdownModels = new ArrayList<>();

        var listClient = clientRepository.findAll();

        for(var c : listClient){
            dropdownModels.add(new DropdownModel(c.getClientName(),c.getClientCode()));
        }

        return new BaseResponse(200,"Success",null,dropdownModels) ;
    }

    public BaseResponse dropdownRegion(){
        List<DropdownModel> dropdownModels = new ArrayList<>();

        var listRegion = masterRegionRepository.findAll();

        for(var r : listRegion){
            dropdownModels.add(new DropdownModel(r.getRegionName(),r.getId()));
        }

        return new BaseResponse(200,"Success",null,dropdownModels) ;
    }

    public BaseResponse dropdownVehicle(String search) {
        List<DropdownModel> dropdownModels = new ArrayList<>();

        var listVehicle = vehicleRepository.getDataCar(search);

        for(var v : listVehicle){
            dropdownModels.add(new DropdownModel(v.getType()+"-"+v.getUniqueNumber(),v.getId()));
        }

        return new BaseResponse(200,"Success",null,dropdownModels) ;
    }

    public BaseResponse dropdownDso(String clientCode){
        List<DropdownModel> dropdownModels = new ArrayList<>();

        var listDso = dsoClientRepository.findByClientCode(clientCode);

        for(var d : listDso){
            dropdownModels.add(new DropdownModel(d.getRegionObj().getRegionName(),d.getId()));
        }

        return new BaseResponse(200,"Success",null,dropdownModels);
    }
}
