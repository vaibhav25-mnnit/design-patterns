package facade;

import services.*;

public class HotelBookingFacade {
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
