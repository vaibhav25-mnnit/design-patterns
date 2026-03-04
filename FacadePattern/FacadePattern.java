public class FacadePattern {
    public static void main(String[] args) {

        // create subsystems once — inject into facade
        HotelBookingFacade facade = new HotelBookingFacade(
            new RoomService(),
            new PaymentService(),
            new InvoiceService(),
            new NotificationService(),
            new LoyaltyService()
        );

        // client only ever calls ONE method — knows nothing about subsystems
        facade.book("Sumit",  "Deluxe Room",  3, 15000.0);
        facade.book("Vaibhav", "Suite Room",   5, 40000.0);
    }
}

// handles room availability and reservation
class RoomService {
    public boolean checkAvailability(String roomType) {
        System.out.println("RoomService    → Checking availability for: " + roomType);
        return true; // assume available
    }

    public void reserveRoom(String guestName, String roomType, int nights) {
        System.out.println("RoomService    → Room reserved for " + guestName +
                           " | " + roomType + " | " + nights + " nights");
    }
}

// handles payment processing
class PaymentService {
    public boolean processPayment(String guestName, double amount) {
        System.out.println("PaymentService → Processing payment of ₹" + amount +
                           " for " + guestName);
        return true;
    }
}

// generates invoice after payment
class InvoiceService {
    public void generateInvoice(String guestName, String roomType, 
                                 int nights, double amount) {
        System.out.println("InvoiceService → Invoice generated for " + guestName +
                           " | " + roomType + " x " + nights +
                           " nights = ₹" + amount);
    }
}

// sends confirmation to guest
class NotificationService {
    public void sendConfirmation(String guestName) {
        System.out.println("NotificationService → Confirmation sent to " + guestName);
    }
}

// adds reward points to guest account
class LoyaltyService {
    public void addPoints(String guestName, double amount) {
        int points = (int) amount / 10;
        System.out.println("LoyaltyService → " + points +
                           " reward points added for " + guestName);
    }
}  

class HotelBookingFacade {

    // HAS-A instance of each subsystem — injected via constructor
    private final RoomService roomService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;
    private final LoyaltyService loyaltyService;

    public HotelBookingFacade(RoomService roomService,
                               PaymentService paymentService,
                               InvoiceService invoiceService,
                               NotificationService notificationService,
                               LoyaltyService loyaltyService) {
        this.roomService         = roomService;
        this.paymentService      = paymentService;
        this.invoiceService      = invoiceService;
        this.notificationService = notificationService;
        this.loyaltyService      = loyaltyService;
    }

    // ONE simple method — hides all the complexity
    public void book(String guestName, String roomType, int nights, double amount) {
        System.out.println("\n======= Booking Started =======");

        // Step 1 — check and reserve room
        if (!roomService.checkAvailability(roomType)) {
            System.out.println("Sorry! " + roomType + " is not available.");
            return;
        }
        roomService.reserveRoom(guestName, roomType, nights);

        // Step 2 — process payment
        if (!paymentService.processPayment(guestName, amount)) {
            System.out.println("Payment failed for " + guestName);
            return;
        }

        // Step 3 — generate invoice
        invoiceService.generateInvoice(guestName, roomType, nights, amount);

        // Step 4 — send confirmation
        notificationService.sendConfirmation(guestName);

        // Step 5 — add loyalty points
        loyaltyService.addPoints(guestName, amount);

        System.out.println("======= Booking Confirmed ✔ =======\n");
    }
}