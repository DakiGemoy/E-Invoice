package co.id.Asset.eInvoice.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "DsoClient")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class DsoClient {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "client_code")
    private String clientCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_code",insertable = false, updatable = false)
    private Client clientObj;

    @Column(name = "region_id")
    private Integer regionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id",insertable = false, updatable = false)
    private MasterRegion regionObj;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

}
