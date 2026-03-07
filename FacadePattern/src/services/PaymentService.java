package services;

public class PaymentService {
    public boolean processPayment(String guestName, double amount) {
        System.out.println("PaymentService → Processing payment of ₹" + amount +
                " for " + guestName);
        return true;
    }
}
