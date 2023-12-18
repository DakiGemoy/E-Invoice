package co.id.Asset.eInvoice.Database.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Client")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Client {
    @Id
    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "name")
    private String clientName;

    @Column(name = "address")
    private String clientAddress;

    @Column(name = "region_id")
    private Integer regionId;

    @Column(name = "email")
    private String clientEmail;

    @Column(name = "phone_number")
    private String clientPhone;

}
