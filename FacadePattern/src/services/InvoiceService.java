package services;

public class InvoiceService {
    public void generateInvoice(String guestName, String roomType,
                                int nights, double amount) {
        System.out.println("InvoiceService → Invoice generated for " + guestName +
                " | " + roomType + " x " + nights +
                " nights = ₹" + amount);
    }
}
