package co.id.Asset.eInvoice.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class InvoiceUpsert {
    private String invoiceNumber;
    private String spkNumber;
    private String notes;
    private String clientCode;
    private LocalDate dueDate;
    private Boolean status;
    private LocalDate createdDate;
    private String createdBy;
    private LocalDate updatedDate;
    private String updatedBy;
}
