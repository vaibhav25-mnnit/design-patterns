import facade.HotelBookingFacade;
import services.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!\nThis is a facade design pattern.");
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