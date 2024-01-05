package co.id.Asset.eInvoice.Util;

import co.id.Asset.eInvoice.Database.Entity.Description;
import co.id.Asset.eInvoice.Database.Entity.DsoClient;
import co.id.Asset.eInvoice.Model.InvoiceSummary;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class Util {

    private static final Locale local = new Locale("id","ID");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyy",local);

    public static String generateEmailMessage(EmailType emailType, InvoiceSummary invoice,
                                              DsoClient client, List<Description> descriptions){
        NumberFormat formatNumber = NumberFormat.getInstance(local);
        String message = "";

        if(emailType.getType().equals("Notification")){
            message = "Kepada Yth. \nPerwakilan "+client.getClientObj().getClientName()+"\n\nDitempat, \n"+
                    "\nBersamaan dengan dikirimkannya email ini, kami informasikan bahwa penyewaan mobil terhadap PT. Bangka Berjaya dengan rincian sebagai berikut : \n"+
                    "\nInvoice No        : "+invoice.getInvoiceNumber()+
                    "\nDibuat pada      : "+formatter.format(invoice.getCreatedDate())+
                    "\nTotal               : Rp. "+formatNumber.format(invoice.getAmount())+
                    "\n\nDengan rincian mobil yang disewa adalah : \n"+
                    "\n"+listDescription(descriptions)+
                    "\nDemikian email ini diinformasikan, apabila ada data yang keliru mohon hubungi pihak kami kembali. Terima kasih."+
                    "\n\n\t\t\t\t\tHormat Kami"+
                    "\n\n\n\t\t\t\tPT. Bangka Berjaya";
        } else {
            message = "Kepada Yth. \nPerwakilan "+client.getClientObj().getClientName()+"\n\nDitempat, \n"+
                    "\nBersamaan dengan dikirimkannya email ini, kami informasikan bahwa penyewaan mobil terhadap PT. Bangka Berjaya dengan rincian sebagai berikut : \n"+
                    "\nInvoice No       : "+invoice.getInvoiceNumber()+
                    "\nDibuat pada      : "+formatter.format(invoice.getCreatedDate())+
                    "\nTotal            : Rp. "+formatNumber.format(invoice.getAmount())+
                    "\n\nDengan rincian mobil yang disewa adalah : \n"+
                    "\n"+listDescription(descriptions)+
                    "\nSudah mendekati waktu pembayaran yang ditentukan, mohon untuk melakukan pembayaran berdasarkan jumlah yang tertera di atas ke rekening dibawah ini : "+
                    "\n\nNo Rekening      : 0418596999"+
                    "\nAtas Nama        : CV. Bangka Berjaya"+
                    "\nBank             : BCA "+
                    "\n\nDemikian email ini diinformasikan, apabila ada data yang keliru mohon hubungi pihak kami kembali. Terima kasih."+
                    "\n\n\t\t\t\t\tHormat Kami"+
                    "\n\n\n\t\t\t\tPT. Bangka Berjaya";
        }

        return message;
    }

    public static String listDescription(List<Description> descriptions){
        String back = "";
        for(var d : descriptions){
            back = back +
                    "Mobil      : "+d.getVehicleObj().getType()+" ("+d.getVehicleObj().getUniqueNumber()+")"+
                  "\nPeriode    : "+formatter.format(d.getRentFrom())+" s/d "+formatter.format(d.getRentTo())+"\n";
        }
        return back;
    }
}
