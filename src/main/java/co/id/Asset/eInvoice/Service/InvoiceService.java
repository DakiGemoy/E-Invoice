package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Entity.Description;
import co.id.Asset.eInvoice.Database.Entity.Invoice;
import co.id.Asset.eInvoice.Database.Handler.InvoiceNotFoundException;
import co.id.Asset.eInvoice.Database.Repository.*;
import co.id.Asset.eInvoice.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MasterRegionRepository masterRegionRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private DescriptionRepository descriptionRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DsoClientRepository dsoClientRepository;

    public BaseResponse getPagingInvoice(String search,
                                         Integer page){
        Integer limit = 5;
        Integer setFetch = 0;
        if(page != 1){
            setFetch = (page-1) * limit;
        }

        var list = invoiceRepository.getListInvoicePagination(search,setFetch,limit);
        var totalData = (double) invoiceRepository.countPaging(search);
        Long totalPage = (long) Math.ceil(totalData/limit);
        PaginationResponse response = new PaginationResponse(list,page,totalPage);

        return new BaseResponse(200,"Success",null,response);
    }

    public List<Invoice> getInvoicePagingModel(String search,
                                               Integer page){
        Integer limit = 5;
        Integer setFetch = 0;
        if(page != 1){
            setFetch = (page-1) * limit;
        }

        return invoiceRepository.getListInvoicePagination(search,setFetch,limit);
    }

    public Long countTotalPageInvoicePagingModel(String search){
        Integer limit = 5;

        var totalData = (double) invoiceRepository.countPaging(search);
        Long totalPage = (long) Math.ceil(totalData/limit);

        return totalPage;
    }

    public Boolean checkInvoiceNumber(String invoiceNumber){
        if(invoiceRepository.existsByInvoiceNumber(invoiceNumber)){
            var inv = invoiceRepository.findById(invoiceNumber).get();

            return inv.getIsDraft();
        } else {
            return false;
        }
    }

    public BaseResponse invoiceNumberGenerator(Integer dsoId){
//        no - invoice = 00+nomor invoice( auto increment)+'/'+kode kantor+'/'+kode client+'/'+Kode Wilayah Client+'/'+bulan dalam angka+'/'+tahun
        var counter = (int) invoiceRepository.count();
        var dsoClient = dsoClientRepository.findById(dsoId)
                .orElseThrow(()->new EntityNotFoundException("Dso Not found : "+dsoId));
        var region = masterRegionRepository.findById(dsoClient.getRegionId()).orElseThrow(
                ()-> new EntityNotFoundException("Region not found"));
        counter++;

        String stringFormat = String.format("%05d",counter)+"/BGK/"+dsoClient.getClientCode()+"/"+region.getRegionShort()+"/"
                + LocalDate.now().getMonthValue()+"/"+LocalDate.now().getYear();
        return new BaseResponse(200,"Success",null,stringFormat);
    }

    @Transactional
    public BaseResponse saveInvoice(InvoiceUpsert payload){
        var load = invoiceRepository.findByInvoiceNumber(payload.getInvoiceNumber());
        Invoice inv = new Invoice();

        if(load.isEmpty()){
            inv = new Invoice(payload.getInvoiceNumber(), payload.getSpkNumber(),
                payload.getNotes(), payload.getDsoClientId(),
                payload.getDueDate(), payload.getStatus());
            inv.setCreatedBy(payload.getCreatedBy());
            inv.setCreatedDate(LocalDateTime.now());

            try {
                invoiceRepository.save(inv);
            } catch (Exception e){
                return new BaseResponse(500,"INTERNAL SERVER ERROR","Error while save invoice to database with message : "+e.getMessage(),null);
            }
        } else {
            inv = load.get();

            inv.setSpkNumber(payload.getSpkNumber());
            inv.setDueDate(payload.getDueDate());
            inv.setIsDraft(payload.getStatus());
            inv.setNotes(payload.getNotes());
            inv.setUpdateBy(payload.getCreatedBy());
            inv.setUpdateDate(LocalDateTime.now());

            try {
                invoiceRepository.save(inv);
            } catch (Exception e){
                return new BaseResponse(500,"INTERNAL SERVER ERROR","Error while update invoice to database with message : "+e.getMessage(),null);
            }
        }

        return new BaseResponse(200,"Success save data",null,null);
    }

    public BaseResponse getInvoice(String invoiceNumber){
        if(invoiceNumber.equals("")){
            return new BaseResponse(200,"OK",null,null);
        } else {
            var invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(
                    ()-> new InvoiceNotFoundException(invoiceNumber));

            var listDesc = descriptionRepository.findByInvoiceNumber(invoiceNumber);

            List<Item> item = new ArrayList<>();
            for (var d : listDesc){
                var v = vehicleRepository.findById(d.getVehicleId()).get();
                item.add(new Item(d.getId(), v.getUniqueNumber()+" "+v.getType(), d.getRentFrom(),d.getRentTo(),d.getPrice()));
            }

            InvoiceUpsert getUpdate = new InvoiceUpsert(invoice.getInvoiceNumber(), invoice.getSpkNumber(),
                    invoice.getNotes(),invoice.getClientDsoId(),invoice.getDueDate(),
                    invoice.getIsDraft(),invoice.getCreatedDate(),invoice.getCreatedBy(), item);

            return new BaseResponse(200,"Ok",null, getUpdate);
        }
    }

    public BaseResponse saveCarToInvoice(CarRequest request){
        Description desc;
        if(request.getDescId()==null){
            desc = new Description(request.getInvoiceNumber(), request.getVehicleId(),
                    request.getRentFrom(), request.getRentTo(), request.getPrice());
        } else {
            var descExists = descriptionRepository.findById(request.getDescId()).get();
            descExists.setVehicleId(request.getVehicleId());
            descExists.setPrice(request.getPrice());
            descExists.setRentFrom(request.getRentFrom());
            descExists.setRentTo(request.getRentTo());

            desc = descExists;
        }

        try {
            descriptionRepository.save(desc);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return new BaseResponse(200,"Success save data",null,null);
    }

    public BaseResponse getDescById(Integer descId){
        return new BaseResponse(200,"Success get Desc",null,descriptionRepository.findById(descId).orElseThrow(
                ()->new EntityNotFoundException("Description not found : "+descId)));
    }

    public BaseResponse postDescUpdate(Description description){
        try {
            descriptionRepository.save(description);
            return new BaseResponse(200,"Success save data",null,null);
        } catch (Exception e){
            throw new RuntimeException("INTERNAL SERVER ERROR WITH MESSAGE : "+e.getMessage());
        }
    }

    public BaseResponse deleteDescription(Integer descId){
        try {
            descriptionRepository.deleteById(descId);
            return new BaseResponse(200,"Success delete description",null,null);
        } catch (Exception e){
            throw new RuntimeException("Error while delete description : "+descId);
        }
     }
}
