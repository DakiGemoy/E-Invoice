package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Repository.DsoClientRepository;
import co.id.Asset.eInvoice.Model.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private DsoClientRepository dsoClientRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(EmailRequest request){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(request.getRecipient());
            mailMessage.setText(request.getMsgBody());
            mailMessage.setSubject(request.getSubject());

            javaMailSender.send(mailMessage);
        }

        catch (Exception e) {
            throw new RuntimeException("Error send email with message : "+e.getMessage());
        }
    }

}
