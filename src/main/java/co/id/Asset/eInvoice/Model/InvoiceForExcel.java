package co.id.Asset.eInvoice.Model;

import co.id.Asset.eInvoice.Database.Entity.Description;
import co.id.Asset.eInvoice.Database.Entity.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Getter @Setter
public class InvoiceForExcel {
    private String createdDate;
    private String notes;
    private String carNumber;
    private String spkNumber;
    private String companyName;
    private String invoiceNo;
    private long total;
    private long pph;
    private long net;
    private String tenggatWaktu = "";

    public InvoiceForExcel (Invoice invoice, Description description){
        Locale locale = new Locale("id","ID");
        DateTimeFormatter fullDate = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
        NumberFormat formatNumber = NumberFormat.getInstance(locale);

        this.createdDate = fullDate.format(invoice.getCreatedDate());
        this.notes = invoice.getNotes()+" : "+description.getVehicleObj().getType()+
                " ( "+invoice.getClientDsoObj().getRegionObj().getRegionName()+
                " ), Periode : "+fullDate.format(description.getRentTo())+" s/d "+
                fullDate.format(description.getRentTo());
        this.carNumber = description.getVehicleObj().getUniqueNumber();
        this.spkNumber = invoice.getSpkNumber();
        this.companyName = invoice.getClientDsoObj().getClientObj().getClientName();
        this.invoiceNo = invoice.getInvoiceNumber();
        this.total = description.getPrice().longValue();
        this.pph = description.getPrice().longValue()*2/100;
        this.net = description.getPrice().longValue() - (description.getPrice().longValue()*2/100);
    }
}
