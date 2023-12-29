package co.id.Asset.eInvoice.Database.Handler;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String invoiceNumber) {
        super("Invoice : "+invoiceNumber + " Not found");
    }
}
