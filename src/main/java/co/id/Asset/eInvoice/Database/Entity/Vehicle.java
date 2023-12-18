package co.id.Asset.eInvoice.Database.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Vehicle")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "unique_number")
    private String uniqueNumber;

    @Column(name = "vehicle_type")
    private String type;

    @Column(name = "rent_period")
    private String rentPeriod;

}
