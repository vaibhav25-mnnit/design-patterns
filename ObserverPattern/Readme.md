# Observer Design Pattern in Java
> A beginner-friendly guide to understanding the Observer Pattern with a real-world Out-of-Stock Notification example.

---

## What is the Observer Pattern?

The Observer Pattern is a **behavioral design pattern** where an object (called **Observable/Subject**) maintains a list of dependents (called **Observers**) and **automatically notifies them** when its state changes.

> Think of it like a **subscription system** — you subscribe once, and whenever something changes, you get notified automatically. You don't keep checking manually.

---

## Real World Analogy

Think of an **Amazon "Notify Me" button** on an out-of-stock product:

```
Product goes OUT OF STOCK
        │
        ▼
Amazon (Observable) notices stock = 0
        │
        ├──► Sends Email to vaibhav        (EmailObserver)
        ├──► Sends Mobile notification to vaibhav  (MobileObserver)
        └──► Sends Mobile notification to sumit    (MobileObserver)

All subscribers are notified automatically.
Nobody had to keep refreshing the page.
```

---

## Project Structure

```
ObserverPattern/
│
└── src/
    ├── Main.java                                    # Client — wires observers to observable
    │
    ├── observable/
    │   ├── OutOfStockObservable.java                # Observable Interface
    │   └── OutOfStockObservableImpl.java            # Concrete Observable — tracks stock
    │
    └── observer/
        ├── NotificationAlertObserver.java           # Observer Interface
        ├── EmailNotificationAlertObserverImpl.java  # Concrete Observer — sends email
        └── MobileNotificationAlertObserverImpl.java # Concrete Observer — sends mobile alert
```

---

## Key Components

| Component | Role | In This Example |
|---|---|---|
| **Observable Interface** | Defines subscribe, unsubscribe, notify | `OutOfStockObservable` |
| **Concrete Observable** | Holds state, notifies on change | `OutOfStockObservableImpl` |
| **Observer Interface** | Defines the `update()` contract | `NotificationAlertObserver` |
| **Concrete Observers** | React when notified | `EmailNotificationAlertObserverImpl`, `MobileNotificationAlertObserverImpl` |
| **Client** | Wires observers to observable | `Main.java` |

---

## The Code

### Observable Interface — Defines the Subject Contract
```java
package observable;

import observer.NotificationAlertObserver;

public interface OutOfStockObservable {
    void add(NotificationAlertObserver observer);     // subscribe
    void remove(NotificationAlertObserver observer);  // unsubscribe
    void notifyObserver();                            // notify all subscribers
    int getData();                                    // get current stock
    void setData(int data);                           // update stock — triggers notify
}
```

### Concrete Observable — Tracks Stock, Notifies on Change
```java
package observable;

import observer.NotificationAlertObserver;
import java.util.ArrayList;
import java.util.List;

public class OutOfStockObservableImpl implements OutOfStockObservable {
    int stock = 0;

    // HAS-A list of all subscribed observers
    List<NotificationAlertObserver> observers = new ArrayList<>();

    @Override
    public void add(NotificationAlertObserver obj) {
        observers.add(obj);        // subscribe
    }

    @Override
    public void remove(NotificationAlertObserver obj) {
        observers.remove(obj);     // unsubscribe
    }

    @Override
    public void notifyObserver() {
        // notify every subscribed observer
        for (NotificationAlertObserver o : observers) o.update();
    }

    @Override
    public int getData() {
        return stock;
    }

    @Override
    public void setData(int stock) {
        if (stock == 0) {
            notifyObserver();      // stock hit 0 — alert everyone immediately
        }
        this.stock = this.stock + stock;
    }
}
```

### Observer Interface — Common Contract for All Observers
```java
package observer;

public interface NotificationAlertObserver {
    void update();   // called by observable when state changes
}
```

### Concrete Observers — React When Notified
```java
// sends email when stock hits 0
package observer;

import observable.OutOfStockObservable;

public class EmailNotificationAlertObserverImpl implements NotificationAlertObserver {
    String userName;
    OutOfStockObservable observable;  // HAS-A observable to read state if needed

    public EmailNotificationAlertObserverImpl(String userName, OutOfStockObservable observable) {
        this.userName   = userName;
        this.observable = observable;
    }

    @Override
    public void update() {
        sendEmail(this.userName);
    }

    private void sendEmail(String userName) {
        System.out.println("Sending out of stock email to :- " + userName);
    }
}

// sends mobile notification when stock hits 0
package observer;

import observable.OutOfStockObservable;

public class MobileNotificationAlertObserverImpl implements NotificationAlertObserver {
    String userName;
    OutOfStockObservable observable;

    public MobileNotificationAlertObserverImpl(String userName, OutOfStockObservable observable) {
        this.observable = observable;
        this.userName   = userName;
    }

    @Override
    public void update() {
        sendMobileNotification(this.userName);
    }

    private void sendMobileNotification(String userName) {
        System.out.println("Sending mobile notification to :- " + userName);
    }
}
```

### Client — Wires Everything Together
```java
import observable.OutOfStockObservable;
import observable.OutOfStockObservableImpl;
import observer.EmailNotificationAlertObserverImpl;
import observer.MobileNotificationAlertObserverImpl;
import observer.NotificationAlertObserver;

public class Main {
    public static void main(String[] args) {

        // create the observable (the thing being watched)
        OutOfStockObservable observable = new OutOfStockObservableImpl();

        // create observers (the ones watching)
        NotificationAlertObserver observer1 = new EmailNotificationAlertObserverImpl("vaibhav", observable);
        NotificationAlertObserver observer2 = new MobileNotificationAlertObserverImpl("vaibhav", observable);
        NotificationAlertObserver observer3 = new MobileNotificationAlertObserverImpl("sumit",   observable);

        // subscribe observers to observable
        observable.add(observer1);
        observable.add(observer2);
        observable.add(observer3);

        observable.setData(10);   // stock = 10, no notification
        observable.setData(0);    // stock = 0,  ALL observers notified
    }
}
```

---

## Output

```
Sending out of stock email to :-         vaibhav
Sending mobile notification to :-        vaibhav
Sending mobile notification to :-        sumit
```

---

## How it Works Step by Step

```
observable.setData(10)
    │
    └──► stock = 10, no notification triggered

observable.setData(0)
    │
    └──► stock == 0 detected
              │
              └──► notifyObserver() called
                        │
                        ├──► observer1.update() → sends email    to vaibhav
                        ├──► observer2.update() → sends mobile   to vaibhav
                        └──► observer3.update() → sends mobile   to sumit
```

---

## The Relationship Between Components

```
OutOfStockObservableImpl
    │
    │  HAS-A List<NotificationAlertObserver>
    │       │
    │       ├──► EmailNotificationAlertObserverImpl   (vaibhav)
    │       ├──► MobileNotificationAlertObserverImpl  (vaibhav)
    │       └──► MobileNotificationAlertObserverImpl  (sumit)
    │
    └── when stock == 0 → calls update() on each observer
```

---

## Why Does Observer HAS-A Observable?

```java
OutOfStockObservable observable;  // observer holds reference to observable
```

So the observer can **pull additional data** from the observable when notified:

```java
@Override
public void update() {
    int currentStock = observable.getData();  // pull current state
    sendEmail("Stock is now: " + currentStock);
}
```

This is called the **Pull Model** — observer fetches what it needs.  
The alternative is the **Push Model** — observable sends data directly in `update(data)`.

| | Push Model | Pull Model (this code) |
|---|---|---|
| **How** | `update(int stock)` | `update()` then `observable.getData()` |
| **Coupling** | Observable knows what data observer needs | Observer decides what to fetch |
| **Flexibility** | Less flexible | More flexible |

---

## Adding a New Observer — Zero Changes to Existing Code

Say you want to add WhatsApp notifications:

```java
// Step 1 — create new observer
public class WhatsAppNotificationObserverImpl implements NotificationAlertObserver {
    public void update() {
        System.out.println("Sending WhatsApp alert to: " + userName);
    }
}

// Step 2 — subscribe in main
observable.add(new WhatsAppNotificationObserverImpl("vaibhav", observable));

// Observable? — ZERO changes ✅
// Existing observers? — ZERO changes ✅
```

---

## Observer vs Other Patterns

### Observer vs Chain of Responsibility

| | Observer | Chain of Responsibility |
|---|---|---|
| **Receivers** | ALL observers notified | ONE handler processes request |
| **Flow** | Broadcast | Sequential chain |
| **Use when** | Multiple listeners need same event | One handler should own the request |

### Observer vs Mediator

| | Observer | Mediator |
|---|---|---|
| **Communication** | Observable → all Observers directly | All objects → Mediator → target objects |
| **Coupling** | Observers know the Observable | Objects only know the Mediator |
| **Use when** | One source, many listeners | Many objects communicating with each other |

---

## SOLID Principles Covered

| Rule | SOLID Principle |
|---|---|
| Observable manages state, Observer reacts to it — separate concerns | Single Responsibility Principle |
| Add new observer type without changing Observable or existing observers | Open/Closed Principle |
| Any `NotificationAlertObserver` can replace another seamlessly | Liskov Substitution Principle |
| Client depends on `OutOfStockObservable` and `NotificationAlertObserver` interfaces, not concrete classes | Dependency Inversion Principle |

---

## When to Use Observer Pattern?

- One object's state change should **automatically trigger** updates in others
- You don't know ahead of time **how many** objects need to be notified
- You want objects to be **loosely coupled** — observable doesn't know concrete observer types
- Common use cases: **event systems, notifications, UI data binding, stock tickers, messaging**

---

## Real World Usage

| Framework / Tool | How Observer is Used |
|---|---|
| `Java EventListener` | Button click notifies all registered listeners |
| `RxJava / Reactor` | Reactive streams — subscribers notified on data change |
| `Spring ApplicationEvent` | Beans listen to application events |
| `Android LiveData` | UI observes data, updates automatically on change |
| `JavaScript addEventListener` | DOM events broadcast to all listeners |

---

## Key Takeaway

> **"Don't poll for changes. Subscribe once and get notified automatically."**

The Observable holds the **state** and the **list of subscribers**.  
The Observer just waits and **reacts** when called.  
They are loosely coupled — Observable never knows the concrete type of its observers.  
Adding or removing observers at runtime requires **zero changes** to the Observable.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)