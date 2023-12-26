package co.id.Asset.eInvoice.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CarRequest {
    private Integer descId;
    private String invoiceNumber;
    private Integer vehicleId;
    private LocalDate rentFrom;
    private LocalDate rentTo;
    private Double price;
}
