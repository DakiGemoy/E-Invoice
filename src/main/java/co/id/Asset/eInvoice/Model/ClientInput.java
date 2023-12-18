package co.id.Asset.eInvoice.Model;

import lombok.Data;

@Data
public class ClientInput {
    private String code;
    private String name;
    private String address;
    private Integer regionId;
    private String email;
    private String phone;
}
