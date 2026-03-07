# Facade Design Pattern in Java

> A beginner-friendly guide to understanding the Facade Pattern with a real-world Hotel Booking example.

---

## What is the Facade Pattern?

The Facade Pattern is a **structural design pattern** that provides a **simple unified interface** to a complex system of classes, hiding all internal complexity from the client.

The word **Facade** literally means the _front face_ of a building — you see a clean entrance, not the wiring, plumbing, and structure behind it.

---

## Real World Analogy

Think of booking a hotel on **MakeMyTrip / Booking.com**:

```
What YOU do (Client):
→ Open app
→ Fill guest details
→ Click "Book Now" ← This is the Facade method

What ACTUALLY happens behind the scenes:
→ RoomService         checks & reserves the room
→ PaymentService      processes your payment
→ InvoiceService      generates your invoice
→ NotificationService sends confirmation email/SMS
→ LoyaltyService      adds reward points to your account
```

You never talk to any of these services directly. The **Booking App is the Facade** — one simple interface hiding all the complexity.

---

## Project Structure

```
FacadePattern/
│
├── FacadePattern.java          # Main file — client code
│
├── HotelBookingFacade.java     # Facade — one simple interface to all subsystems
│
└── services/                   # Subsystems — complex classes behind the scenes
    ├── RoomService.java
    ├── PaymentService.java
    ├── InvoiceService.java
    ├── NotificationService.java
    └── LoyaltyService.java
```

---

## Key Components

| Component      | Role                                         | In This Example                       |
| -------------- | -------------------------------------------- | ------------------------------------- |
| **Subsystems** | Complex classes doing actual work            | `RoomService`, `PaymentService`, etc. |
| **Facade**     | Coordinates all subsystems, simple interface | `HotelBookingFacade`                  |
| **Client**     | Talks only to Facade, unaware of subsystems  | `FacadePattern.java`                  |

---

## The Code

### Subsystems — Complex Classes Behind the Scenes

```java
// handles room availability and reservation
class RoomService {
    public boolean checkAvailability(String roomType) {
        System.out.println("RoomService    → Checking availability for: " + roomType);
        return true;
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
                           " | " + roomType + " x " + nights + " nights = ₹" + amount);
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
```

### Facade — One Simple Interface to All Subsystems

```java
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
```

### Client — Only Talks to Facade

```java
public class FacadePattern {
    public static void main(String[] args) {

        // create subsystems once — inject into facade via constructor
        HotelBookingFacade facade = new HotelBookingFacade(
            new RoomService(),
            new PaymentService(),
            new InvoiceService(),
            new NotificationService(),
            new LoyaltyService()
        );

        // client only ever calls ONE method — knows nothing about subsystems
        facade.book("John",  "Deluxe Room", 3, 15000.0);
        facade.book("Sarah", "Suite Room",  5, 40000.0);
    }
}
```

---

## Output

```
======= Booking Started =======
RoomService         → Checking availability for: Deluxe Room
RoomService         → Room reserved for John | Deluxe Room | 3 nights
PaymentService      → Processing payment of ₹15000.0 for John
InvoiceService      → Invoice generated for John | Deluxe Room x 3 nights = ₹15000.0
NotificationService → Confirmation sent to John
LoyaltyService      → 1500 reward points added for John
======= Booking Confirmed ✔ =======

======= Booking Started =======
RoomService         → Checking availability for: Suite Room
RoomService         → Room reserved for Sarah | Suite Room | 5 nights
PaymentService      → Processing payment of ₹40000.0 for Sarah
InvoiceService      → Invoice generated for Sarah | Suite Room x 5 nights = ₹40000.0
NotificationService → Confirmation sent to Sarah
LoyaltyService      → 4000 reward points added for Sarah
======= Booking Confirmed ✔ =======
```

---

## What Client Sees vs What Actually Happens

```
CLIENT                        FACADE                         SUBSYSTEMS
──────                        ──────                         ──────────
                                                        ┌──► RoomService
facade.book("John",      ───► coordinates          ─────┼──► PaymentService
            "Deluxe",         all steps                 ├──► InvoiceService
            3,                in right order            ├──► NotificationService
            15000.0)                                    └──► LoyaltyService
```

---

## Rules to Always Follow

### 1. Facade HAS-A instance of every subsystem it coordinates

```java
class HotelBookingFacade {
    private final RoomService roomService;         // HAS-A
    private final PaymentService paymentService;   // HAS-A
    private final NotificationService notifService;// HAS-A
}
```

### 2. Always inject subsystems via Constructor — never create internally

```java
// ❌ BAD — tightly coupled, hard to test
class HotelBookingFacade {
    private RoomService roomService = new RoomService(); // created internally
}

// ✅ GOOD — injected from outside, easy to swap and test
class HotelBookingFacade {
    private final RoomService roomService;
    public HotelBookingFacade(RoomService roomService, ...) {
        this.roomService = roomService;  // injected via constructor
    }
}
```

### 3. Subsystems should NOT know about the Facade

```java
// RoomService, PaymentService etc work completely independently
// They have no reference to HotelBookingFacade
// This keeps them reusable and testable on their own
```

### 4. Facade does NOT restrict access to subsystems

```java
// Client CAN still access subsystems directly if needed
RoomService room = new RoomService();
room.checkAvailability("Deluxe"); // perfectly valid

// Facade just provides a simpler path — it does not lock anything
```

### 5. You can have multiple Facades for the same subsystem

```java
// Different facades for different client needs
BasicBookingFacade   facade1 = new BasicBookingFacade(room, payment);
PremiumBookingFacade facade2 = new PremiumBookingFacade(room, payment, loyalty, concierge);
```

---

## Facade vs Other Patterns

### Facade vs Adapter

|                  | Facade                  | Adapter                        |
| ---------------- | ----------------------- | ------------------------------ |
| **Intent**       | Simplify complex system | Convert incompatible interface |
| **Wraps**        | Multiple subsystems     | One legacy class               |
| **Legacy code?** | Not necessarily         | Always                         |
| **Analogy**      | Zomato app              | USB dongle                     |

### Facade vs Mediator

|                       | Facade                         | Mediator                                |
| --------------------- | ------------------------------ | --------------------------------------- |
| **Direction**         | One way — client to subsystems | Two way — subsystems talk to each other |
| **Subsystems aware?** | No                             | Yes, they know the mediator             |
| **Purpose**           | Simplify interface             | Control communication                   |

### Facade vs Proxy

|               | Facade             | Proxy                            |
| ------------- | ------------------ | -------------------------------- |
| **Wraps**     | Multiple classes   | One class                        |
| **Purpose**   | Simplify           | Control access, lazy load, cache |
| **Interface** | New simplified one | Same as original                 |

---

## SOLID Principles Covered

| Rule                                               | SOLID Principle                 |
| -------------------------------------------------- | ------------------------------- |
| Facade only coordinates, subsystems do actual work | Single Responsibility Principle |
| Add new subsystem without changing client          | Open/Closed Principle           |
| Client depends on Facade, not subsystems directly  | Dependency Inversion Principle  |
| Subsystems work independently without Facade       | Interface Segregation Principle |

---

## Real World Usage

| Framework / Library   | How Facade is Used                                        |
| --------------------- | --------------------------------------------------------- |
| `slf4j`               | Simple logging interface hiding Log4j, Logback complexity |
| `Spring JdbcTemplate` | Facade over raw JDBC boilerplate                          |
| `Retrofit`            | Facade over raw HTTP calls                                |
| `javax.faces (JSF)`   | Facade over Servlet and JSP complexity                    |

---

## When to Use Facade Pattern?

- Client has to interact with **too many classes** to get one thing done
- You want to provide a **simple entry point** to a complex subsystem
- You want to **layer your system** — high level facade over low level subsystems
- You want subsystems to be **independently developed and tested**

---

## Key Takeaway

> **"Don't make the client do the hard work. Give them one door to walk through."**

The client should not know or care how many services coordinate behind the scenes. That is the Facade's job — absorb the complexity, expose the simplicity.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)

---
