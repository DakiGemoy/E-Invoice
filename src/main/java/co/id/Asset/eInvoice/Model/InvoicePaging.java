package co.id.Asset.eInvoice.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class InvoicePaging {
    private String search = "";
    private Integer page = 1;
    private String rangeFrom ="";
    private String rangeTo="";
}
