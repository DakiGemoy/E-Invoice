package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Repository.ClientRepository;
import co.id.Asset.eInvoice.Database.Repository.InvoiceRepository;
import co.id.Asset.eInvoice.Database.Repository.MasterRegionRepository;
import co.id.Asset.eInvoice.Model.BaseResponse;
import co.id.Asset.eInvoice.Model.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
public class InvoiceService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MasterRegionRepository masterRegionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public BaseResponse createInvoice(String clientCode){
        return new BaseResponse(200,"Success",null, invoiceNumberGenerator(clientCode));
    }

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

    private String invoiceNumberGenerator(String clientCode){
//        no - invouce = 00+nomor invoice( auto increment)+'/'+kode kantor+'/'+kode client+'/'+Kode Wilayah Client+'/'+bulan dalam angka+'/'+tahun
        var counter = (int) invoiceRepository.count();
        var client = clientRepository.findById(clientCode).orElseThrow(
                ()-> new EntityNotFoundException("Client not found"));
        var region = masterRegionRepository.findById(client.getRegionId()).orElseThrow(
                ()-> new EntityNotFoundException("Region not found"));
        counter++;

        String stringFormat = "00"+counter+"/BGK/"+clientCode+"/"+region.getRegionShort()+"/"
                + LocalDate.now().getMonthValue()+"/"+LocalDate.now().getYear();
        return stringFormat;
    }
}
