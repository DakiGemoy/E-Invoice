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
    private Long id;

    @Column(name = "invoice")
    private String invoiceNumber;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "price")
    private Double price;

    @Column(name = "rent_from")
    private LocalDate rentFrom;

    @Column(name = "rent_to")
    private LocalDate rentTo;

    public Description(String invoiceNumber, Integer vehicleId,
                       LocalDate rentFrom, LocalDate rentTo) {
        this.invoiceNumber = invoiceNumber;
        this.vehicleId = vehicleId;
        this.rentTo = rentTo;
        this.rentFrom = rentFrom;
    }

}
