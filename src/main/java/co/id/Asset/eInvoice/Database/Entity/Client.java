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

}
