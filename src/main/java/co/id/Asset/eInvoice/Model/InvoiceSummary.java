package co.id.Asset.eInvoice.Model;

import co.id.Asset.eInvoice.Database.Entity.Invoice;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class InvoiceSummary {
    private String invoiceNumber;
    private String spkNumber;
    private LocalDate dueDate;
    private Boolean isDraft;
    private Double amount;

    public InvoiceSummary(Invoice invoice, Double totalPrice){
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.spkNumber = invoice.getSpkNumber();
        this.dueDate = invoice.getDueDate();
        this.isDraft = invoice.getIsDraft();
        this.amount = totalPrice;
    }

    public InvoiceSummary (Invoice invoice){
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.spkNumber = invoice.getSpkNumber();
        this.dueDate = invoice.getDueDate();
        this.isDraft = invoice.getIsDraft();
    }
}
