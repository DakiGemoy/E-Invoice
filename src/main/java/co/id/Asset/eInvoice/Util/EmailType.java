package co.id.Asset.eInvoice.Util;

public enum EmailType {
    NOTIFICATION("Notification"),
    REMINDER("Reminder");

    private String type;

    EmailType (String type){this.type = type;}

    public String getType(){return this.type;}
}
