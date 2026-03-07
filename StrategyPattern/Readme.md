# Strategy Design Pattern in Java
> A beginner-friendly guide to understanding the Strategy Pattern with a real-world Vehicle Driving example.

---

## What is the Strategy Pattern?

The Strategy Pattern is a **behavioral design pattern** that defines a family of algorithms, puts each one in a separate class, and makes them **interchangeable at runtime**.

> Instead of hardcoding behaviour inside a class, you inject the behaviour from outside. The class delegates the work to the injected strategy.

---

## Real World Analogy

Think of **Google Maps navigation modes**:

```
Same app (Vehicle), different strategies:

→ Driving mode    → NormalDriveStrategy
→ Off-road mode   → OffRoadDriveStrategy
→ Sports mode     → SportsDriveStrategy
```

The map app doesn't change. Only the **strategy** for how to navigate changes. You pick the strategy, the app executes it.

---

## Project Structure

```
StrategyPattern/
│
└── src/
    ├── Main.java                              # Client — creates vehicles and calls drive()
    │
    ├── entity/
    │   ├── Vehicle.java                       # Base class — HAS-A DriveStrategy
    │   ├── NormalVehicle.java                 # Injects NormalDriveStrategy
    │   ├── OffRoadVehicle.java                # Injects OffRoadDriveStrategy
    │   └── SportzVehicle.java                 # Injects SportsDriveStrategy
    │
    └── Stratigies/Drive/
        ├── DriveStrategy.java                 # Strategy Interface
        ├── NormalDriveStrategy.java           # Concrete Strategy
        ├── OffRoadDriveStrategy.java          # Concrete Strategy
        └── SportsDriveStrategy.java           # Concrete Strategy
```

---

## Key Components

| Component | Role | In This Example |
|---|---|---|
| **Strategy Interface** | Common contract for all algorithms | `DriveStrategy` |
| **Concrete Strategies** | Individual algorithm implementations | `NormalDriveStrategy`, `OffRoadDriveStrategy`, `SportsDriveStrategy` |
| **Context** | HAS-A strategy, delegates work to it | `Vehicle` |
| **Concrete Contexts** | Pick and inject their own strategy | `NormalVehicle`, `OffRoadVehicle`, `SportzVehicle` |
| **Client** | Creates context with desired strategy | `Main.java` |

---

## The Code

### Strategy Interface — Common Contract for All Drive Behaviours
```java
package Stratigies.Drive;

public interface DriveStrategy {
    public void drive();
}
```

### Concrete Strategies — Each Algorithm in Its Own Class
```java
// normal road driving
package Stratigies.Drive;

public class NormalDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving vehicle with normal strategy.");
    }
}

// off-road driving
package Stratigies.Drive;

public class OffRoadDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving vehicle with offroad strategy.");
    }
}

// sports driving
package Stratigies.Drive;

public class SportsDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving vehicle with sportz strategy.");
    }
}
```

### Context — HAS-A Strategy, Delegates Drive to It
```java
package entity;

import Stratigies.Drive.DriveStrategy;

public class Vehicle {
    DriveStrategy driveStrategy;   // HAS-A strategy — injected via constructor

    public Vehicle(DriveStrategy driveStrategy) {
        this.driveStrategy = driveStrategy;
    }

    public void drive() {
        driveStrategy.drive();     // delegates to strategy — Vehicle doesn't know HOW
    }
}
```

### Concrete Contexts — Each Vehicle Picks Its Own Strategy
```java
// NormalVehicle always uses NormalDriveStrategy
package entity;

public class NormalVehicle extends Vehicle {
    public NormalVehicle() {
        super(new NormalDriveStrategy());   // strategy injected at creation
    }
}

// OffRoadVehicle always uses OffRoadDriveStrategy
package entity;

public class OffRoadVehicle extends Vehicle {
    public OffRoadVehicle() {
        super(new OffRoadDriveStrategy());
    }
}

// SportzVehicle always uses SportsDriveStrategy
package entity;

public class SportzVehicle extends Vehicle {
    public SportzVehicle() {
        super(new SportsDriveStrategy());
    }
}
```

### Client — Creates Vehicles and Calls drive()
```java
public class Main {
    public static void main(String[] args) {

        NormalVehicle  normalVehicle  = new NormalVehicle();
        OffRoadVehicle offRoadVehicle = new OffRoadVehicle();
        SportzVehicle  sportzVehicle  = new SportzVehicle();

        normalVehicle.drive();    // delegates to NormalDriveStrategy
        offRoadVehicle.drive();   // delegates to OffRoadDriveStrategy
        sportzVehicle.drive();    // delegates to SportsDriveStrategy
    }
}
```

---

## Output

```
Driving vehicle with normal strategy.
Driving vehicle with offroad strategy.
Driving vehicle with sportz strategy.
```

---

## How It Works Internally

```
normalVehicle.drive()
        │
        ▼
Vehicle.drive()
        │
        └──► driveStrategy.drive()
                    │
                    ▼
        NormalDriveStrategy.drive()
                    │
                    ▼
        "Driving vehicle with normal strategy."
```

`Vehicle` never knows HOW to drive. It just asks its strategy to do it.

---

## Without Strategy vs With Strategy

```java
// ❌ WITHOUT Strategy — behaviour hardcoded using if/else
public class Vehicle {
    String type;
    public void drive() {
        if (type.equals("normal"))  { System.out.println("Normal drive");  }
        if (type.equals("offroad")) { System.out.println("Offroad drive"); }
        if (type.equals("sportz"))  { System.out.println("Sportz drive");  }
        // adding new vehicle type = modifying this class every time ❌
    }
}

// ✅ WITH Strategy — behaviour injected, class never changes
public class Vehicle {
    DriveStrategy driveStrategy;
    public void drive() {
        driveStrategy.drive();  // adding new vehicle type = new strategy class only ✅
    }
}
```

---

## Adding a New Vehicle Type — Only One New Class Needed

Say you want to add an Electric Vehicle:

```java
// Step 1 — new strategy
public class ElectricDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving vehicle with silent electric strategy.");
    }
}

// Step 2 — new vehicle
public class ElectricVehicle extends Vehicle {
    public ElectricVehicle() {
        super(new ElectricDriveStrategy());
    }
}

// Vehicle base class? — ZERO changes ✅
// Existing strategies? — ZERO changes ✅
// Existing vehicles?  — ZERO changes ✅
```

---

## Strategy Can Also Be Swapped at Runtime

```java
// same vehicle, different strategy at runtime
Vehicle vehicle = new NormalVehicle();
vehicle.drive();  // normal driving

// swap strategy on the fly
vehicle.driveStrategy = new OffRoadDriveStrategy();
vehicle.drive();  // now offroad driving — same object, different behaviour
```

This is the real power of Strategy — **behaviour can change without changing the object**.

---

## Strategy vs Other Patterns

### Strategy vs Template Method

| | Strategy | Template Method |
|---|---|---|
| **Mechanism** | Composition (HAS-A) | Inheritance (IS-A) |
| **Algorithm defined** | In separate strategy class | In base class with hooks |
| **Swap at runtime?** | ✅ Yes | ❌ No |
| **Preferred?** | ✅ Yes — more flexible | Only when inheritance makes sense |

### Strategy vs State

| | Strategy | State |
|---|---|---|
| **Purpose** | Switch algorithms | Switch behaviour based on internal state |
| **Who switches?** | Client injects strategy | Object switches its own state |
| **Awareness** | Strategies unaware of each other | States may transition to each other |

---

## SOLID Principles Covered

| Rule | SOLID Principle |
|---|---|
| Each strategy class has one driving algorithm only | Single Responsibility Principle |
| Add new drive behaviour without touching `Vehicle` | Open/Closed Principle |
| Any `DriveStrategy` is substitutable for another | Liskov Substitution Principle |
| `Vehicle` depends on `DriveStrategy` interface, not concrete strategies | Dependency Inversion Principle |

---

## When to Use Strategy Pattern?

- You have **multiple variants of an algorithm** and want to switch between them
- You want to **eliminate large if/else or switch blocks** based on type
- You want behaviour to be **changeable at runtime**
- Common use cases: **sorting algorithms, payment methods, compression, navigation modes, discount strategies**

---

## Real World Usage

| Framework / Tool | How Strategy is Used |
|---|---|
| `java.util.Comparator` | Sorting strategy injected into `Collections.sort()` |
| `Spring Security` | Authentication strategies (JWT, OAuth, Basic Auth) |
| `javax.servlet.Filter` | Different request handling strategies |
| `Payment Gateways` | PayPal, Stripe, Razorpay as interchangeable strategies |

---

## Key Takeaway

> **"Don't hardcode behaviour inside a class. Inject it from outside and let it be swappable."**

`Vehicle` knows it needs to `drive()` — that is the WHAT.  
`NormalDriveStrategy`, `OffRoadDriveStrategy` know HOW to drive — that is the HOW.  
`Vehicle` never cares about the HOW — it just delegates to whatever strategy it holds.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)