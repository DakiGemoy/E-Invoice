package co.id.Asset.eInvoice.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class EmailRequest {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailRequest(String recipient, String msgBody,
                        String subject){
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }
}
