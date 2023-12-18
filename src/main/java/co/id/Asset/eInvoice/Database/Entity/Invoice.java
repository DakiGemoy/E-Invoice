package co.id.Asset.eInvoice.Database.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Invoice")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Invoice {
    @Id
    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "spk_number")
    private String spkNumber;

    @Column(name = "notes")
    private String notes;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "is_draft")
    private Boolean isDraft;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
