package co.id.Asset.eInvoice.Database.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DescriptionList")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "invoice")
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice",insertable = false, updatable = false)
    private Invoice invoiceObj;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",insertable = false, updatable = false)
    private Vehicle vehicleObj;

    @Column(name = "price")
    private Double price;

    @Column(name = "rent_from")
    private LocalDate rentFrom;

    @Column(name = "rent_to")
    private LocalDate rentTo;

    public Description(String invoiceNumber, Integer vehicleId,
                       LocalDate rentFrom, LocalDate rentTo, Double price) {
        this.invoiceNumber = invoiceNumber;
        this.vehicleId = vehicleId;
        this.rentTo = rentTo;
        this.rentFrom = rentFrom;
        this.price = price;
    }

}
