package co.id.Asset.eInvoice.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationResponse {
    private Object detail;
    private Integer page;
    private Long totalPage;
}
