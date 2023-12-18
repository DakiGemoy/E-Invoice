package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Entity.Description;
import co.id.Asset.eInvoice.Database.Entity.Invoice;
import co.id.Asset.eInvoice.Database.Handler.InvoiceNotFoundException;
import co.id.Asset.eInvoice.Database.Repository.ClientRepository;
import co.id.Asset.eInvoice.Database.Repository.DescriptionRepository;
import co.id.Asset.eInvoice.Database.Repository.InvoiceRepository;
import co.id.Asset.eInvoice.Database.Repository.MasterRegionRepository;
import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.InvoiceUpsert;
import co.id.Asset.eInvoice.Model.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public BaseResponse getPagingInvoice(String invoiceNumber,
                                         String spkNumber,
                                         Integer page){
        Integer limit = 5;
        Integer setFetch = 0;
        if(page != 1){
            setFetch = (page-1) * limit;
        }

        var list = invoiceRepository.getListInvoicePagination(invoiceNumber,spkNumber,setFetch,limit);
        var totalData = (double) invoiceRepository.countPaging(invoiceNumber,spkNumber);
        Long totalPage = (long) Math.ceil(totalData/limit);
        PaginationResponse response = new PaginationResponse(list,page,totalPage);

        return new BaseResponse(200,"Success",null,response);
    }

    public BaseResponse invoiceNumberGenerator(String clientCode){
//        no - invoice = 00+nomor invoice( auto increment)+'/'+kode kantor+'/'+kode client+'/'+Kode Wilayah Client+'/'+bulan dalam angka+'/'+tahun
        var counter = (int) invoiceRepository.count();
        var client = clientRepository.findById(clientCode).orElseThrow(
                ()-> new EntityNotFoundException("Client not found"));
        var region = masterRegionRepository.findById(client.getRegionId()).orElseThrow(
                ()-> new EntityNotFoundException("Region not found"));
        counter++;

        String stringFormat = String.format("%05d",counter)+"/BGK/"+clientCode+"/"+region.getRegionShort()+"/"
                + LocalDate.now().getMonthValue()+"/"+LocalDate.now().getYear();
        return new BaseResponse(200,"Success",null,stringFormat);
    }

    @Transactional
    public BaseResponse saveInvoice(InvoiceUpsert payload){
        var load = invoiceRepository.findByInvoiceNumber(payload.getInvoiceNumber());
        Invoice inv = new Invoice();

        if(load.isEmpty()){
            inv = new Invoice(payload.getInvoiceNumber(), payload.getSpkNumber(),
                payload.getNotes(), payload.getClientCode(),
                payload.getDueDate(), payload.getStatus());
            inv.setCreatedBy(payload.getCreatedBy());
            inv.setCreatedDate(LocalDateTime.now());

            try {
                invoiceRepository.save(inv);
            } catch (Exception e){
                return new BaseResponse(500,"INTERNAL SERVER ERROR","Error while save invoice to database with message : "+e.getMessage(),null);
            }

            for(var d : payload.getDesc()){
                Description desc = new Description(payload.getInvoiceNumber(),d.getVehicleId(),
                        d.getRentFrom(), d.getRentTo());
                try {
                   descriptionRepository.save(desc);
                } catch (Exception e){
                    return new BaseResponse(500,"INTERNAL SERVER ERROR","Error while save description to database with message : "+e.getMessage(),null);
                }
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

            for(var d : payload.getDesc()){
                var descExist = descriptionRepository.
                        findByInvoiceNumberAndVehicleId(payload.getInvoiceNumber(),d.getVehicleId()).orElseThrow(
                                ()-> new EntityNotFoundException("Item not found for : "+payload.getInvoiceNumber()+" - "+d.getVehicleId()));
                descExist.setRentFrom(d.getRentFrom());
                descExist.setRentTo(d.getRentTo());

                try {
                    descriptionRepository.save(descExist);
                } catch (Exception e){
                    return new BaseResponse(500,"INTERNAL SERVER ERROR","Error while update description to database with message : "+e.getMessage(),null);
                }
            }
        }

        return new BaseResponse(200,"Success save data",null,null);
    }

    public BaseResponse getInvoice(String invoiceNumber){
        var invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(
                ()-> new InvoiceNotFoundException(invoiceNumber));
        var descriptions = descriptionRepository.getDescByInvoiceNumber(invoiceNumber);

        InvoiceUpsert getUpdate = new InvoiceUpsert(invoice.getInvoiceNumber(), invoice.getSpkNumber(),
                invoice.getNotes(),invoice.getClientCode(),invoice.getDueDate(),
                invoice.getIsDraft(),invoice.getCreatedDate(),invoice.getCreatedBy(),descriptions);

        return new BaseResponse(200,"Ok",null, getUpdate);
    }
}
