package co.id.Asset.eInvoice.Model;

import co.id.Asset.eInvoice.Database.Entity.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class InvoiceUpsert {
    private String invoiceNumber;
    private String spkNumber;
    private String notes;
    private String clientCode;
    private LocalDate dueDate;
    private Boolean status = true; //default status is drafting
    private LocalDateTime createdDate;
    private String createdBy;
    private List<Item> desc;
}
