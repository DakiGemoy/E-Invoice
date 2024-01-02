package co.id.Asset.eInvoice.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(name = "client_dso_id")
    private Integer clientDsoId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER  )
    @JoinColumn(name = "client_dso_id",insertable = false, updatable = false)
    private DsoClient clientDsoObj;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_draft")
    private Boolean isDraft;

    @Column(name = "sendToExcel")
    private Boolean sendToExcel;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public Invoice(String invoiceNumber, String spkNumber, String notes,
                   Integer clientDsoId, LocalDate dueDate, Boolean isDraft){
        this.invoiceNumber = invoiceNumber;
        this.spkNumber = spkNumber;
        this.notes = notes;
        this.clientDsoId = clientDsoId;
        this.dueDate = dueDate;
        this.isDraft = isDraft;
    }

}
