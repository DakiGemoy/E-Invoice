package co.id.Asset.eInvoice.Model;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Data
public class BaseResponse {
    private int code;
    private String message;
    private String exception;
    private Object data;
}
