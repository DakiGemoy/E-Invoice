package co.id.Asset.eInvoice.Model;

import co.id.Asset.eInvoice.Database.Entity.Invoice;
import lombok.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class InvoiceSummary {
    private String invoiceNumber;
    private String spkNumber;
    private LocalDate dueDate;
    private LocalDate createdDate;
    private Boolean isDraft;
    private Boolean isReminder;
    private Double amount;
    private String amountString;

    public InvoiceSummary(Invoice invoice, Double totalPrice){
        Locale locale = new Locale("id","ID");
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.spkNumber = invoice.getSpkNumber();
        this.dueDate = invoice.getDueDate();
        this.isDraft = invoice.getIsDraft();
        this.isReminder = invoice.getIsReminder();
        this.amount = totalPrice;
        this.createdDate = LocalDate.from(invoice.getCreatedDate());
        this.amountString = "Rp. "+numberFormat.format(totalPrice);
    }

    public InvoiceSummary (Invoice invoice){
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.spkNumber = invoice.getSpkNumber();
        this.dueDate = invoice.getDueDate();
        this.isDraft = invoice.getIsDraft();
        this.isReminder = invoice.getIsReminder();
        this.createdDate = LocalDate.from(invoice.getCreatedDate());
    }
}
