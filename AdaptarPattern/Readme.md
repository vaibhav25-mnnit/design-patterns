# Adapter Design Pattern in Java

> A beginner-friendly guide to understanding the Adapter Pattern with a real-world USB cable example.

---

## What is the Adapter Pattern?

The Adapter Pattern is a **structural design pattern** that allows two incompatible interfaces to work together.

Think of it like a **USB dongle** on your laptop:

- Your laptop only has a **USB-C port** (the interface your code understands)
- Your old devices have **Type-A, Micro-USB, HDMI** ports (legacy systems)
- The **dongle/converter** in between is the Adapter

You don't rewire your laptop or your old device. You just put an adapter in between.

---

## Real World Analogy

```
Old Device (Type-A)          Dongle (Adapter)          Laptop (Client)
────────────────────         ────────────────          ───────────────
TypeAcable           ◄─────  TypeAAdapter   ─────►    UsbCPort (interface)
MicroUsbCable        ◄─────  MicroUsbAdapter ────►    UsbCPort (interface)
HdmiCable            ◄─────  HdmiAdapter    ─────►    UsbCPort (interface)
```

---

## Project Structure

```
AdapterPattern/
│
├── AdapterPattern.java       # Main file — wires everything together
│
├── UsbCPort.java             # Target Interface — what the Laptop understands
│
├── legacy/                   # Legacy systems — never modified
│   ├── TypeAcable.java
│   ├── TypeBcable.java
│   ├── MicroUsbCable.java
│   └── HdmiCable.java
│
└── adapters/                 # One adapter per legacy class
    ├── TypeAAdapter.java
    ├── TypeBAdapter.java
    ├── MicroUsbAdapter.java
    └── HdmiAdapter.java
```

---

## Key Components

| Component            | Role                           | In This Example                     |
| -------------------- | ------------------------------ | ----------------------------------- |
| **Target Interface** | What the client understands    | `UsbCPort`                          |
| **Adaptee**          | Legacy system, never touched   | `TypeAcable`, `HdmiCable`, etc.     |
| **Adapter**          | Bridges adaptee to target      | `TypeAAdapter`, `HdmiAdapter`, etc. |
| **Client**           | Uses only the target interface | `Laptop`                            |
| **Main**             | Wires everything together      | `AdapterPattern.java`               |

---

## The Code

```java
// TARGET INTERFACE — what Laptop (client) understands
interface UsbCPort {
    void connect(String deviceName);
}

// LEGACY SYSTEMS — never modified
class TypeAcable {
    public void connectViaTypeA(String deviceName) {
        System.out.println("[Type-A] Connected: " + deviceName);
    }
}

class HdmiCable {
    public void connectViaHdmi(String deviceName) {
        System.out.println("[HDMI] Connected: " + deviceName);
    }
}

// ADAPTERS — one per legacy class
class TypeAAdapter implements UsbCPort {          // IS-A  UsbCPort
    private final TypeAcable typeAcable;          // HAS-A TypeAcable

    public TypeAAdapter(TypeAcable typeAcable) {  // injected via constructor
        this.typeAcable = typeAcable;
    }

    @Override
    public void connect(String deviceName) {
        System.out.print("USB-C → Type-A Adapter → ");
        typeAcable.connectViaTypeA(deviceName);   // delegates to legacy
    }
}

class HdmiAdapter implements UsbCPort {
    private final HdmiCable hdmiCable;

    public HdmiAdapter(HdmiCable hdmiCable) {
        this.hdmiCable = hdmiCable;
    }

    @Override
    public void connect(String deviceName) {
        System.out.print("USB-C → HDMI Adapter → ");
        hdmiCable.connectViaHdmi(deviceName);
    }
}

// CLIENT — only knows about UsbCPort interface, never concrete adapters
class Laptop implements UsbCPort {
    private final Map<String, UsbCPort> adapterMap; // adapters injected via constructor

    public Laptop(Map<String, UsbCPort> adapterMap) {
        this.adapterMap = adapterMap;
    }

    @Override
    public void connect(String deviceName) {
        System.out.println("USB-C Native → Connected: " + deviceName);
    }

    public void connect(String cableType, String deviceName) {
        if (cableType.equalsIgnoreCase("usbc")) {
            System.out.println("USB-C Native → Connected: " + deviceName);
            return;
        }
        UsbCPort adapter = adapterMap.get(cableType.toLowerCase());
        if (adapter != null) {
            adapter.connect(deviceName);
        } else {
            System.out.println("No adapter found for: " + cableType);
        }
    }
}

// MAIN — the only place where everything is wired together
public class AdapterPattern {
    public static void main(String[] args) {

        // create adaptees once
        TypeAcable    typeA    = new TypeAcable();
        TypeBcable    typeB    = new TypeBcable();
        MicroUsbCable microUsb = new MicroUsbCable();
        HdmiCable     hdmi     = new HdmiCable();

        // wrap in adapters once — stored in map and reused, never recreated
        Map<String, UsbCPort> adapterMap = new HashMap<>();
        adapterMap.put("typea",    new TypeAAdapter(typeA));
        adapterMap.put("typeb",    new TypeBAdapter(typeB));
        adapterMap.put("microusb", new MicroUsbAdapter(microUsb));
        adapterMap.put("hdmi",     new HdmiAdapter(hdmi));

        // inject map into Laptop via constructor
        Laptop laptop = new Laptop(adapterMap);

        // connect devices — adapters reused, not recreated
        laptop.connect("usbc",     "Samsung SSD");
        laptop.connect("typea",    "Old USB Drive");
        laptop.connect("typea",    "Keyboard");
        laptop.connect("microusb", "Old Android Phone");
        laptop.connect("hdmi",     "External Monitor");
        laptop.connect("vga",      "Old Projector");  // no adapter — graceful message
    }
}
```

---

## Output

```
USB-C      Native port → Connected: Samsung SSD
USB-C → Type-A Adapter → [Type-A]    Connected: Old USB Drive
USB-C → Type-A Adapter → [Type-A]    Connected: Keyboard
USB-C → Micro-USB Adapter → [Micro-USB] Connected: Old Android Phone
USB-C → HDMI Adapter → [HDMI]      Connected: External Monitor
No adapter found for cable type: vga
```

---

## Rules to Always Follow

### 1. Never Touch Legacy Code

The adaptee classes already work fine on their own. Your job is to wrap them, not change them.

```java
// TypeAcable, HdmiCable etc — leave exactly as they are
```

### 2. One Adapter per Legacy Class — Strictly

Each adapter has one job: adapt one legacy class. Never mix two legacy systems in one adapter.

```java
// ❌ WRONG — one adapter managing multiple legacy systems
class MediaAdapter {
    private VlcPlayer vlc;
    private Mp4Player mp4;  // NO — not your responsibility
}

// ✅ RIGHT — one adapter, one legacy system
class VlcAdapter { private final VlcPlayer vlc; }
class Mp4Adapter { private final Mp4Player mp4; }
```

### 3. Adapter HAS-A its Own Legacy Class (Composition)

```java
class TypeAAdapter implements UsbCPort {
    private final TypeAcable typeAcable;  // HAS-A — composition, not inheritance
}
```

### 4. Adapter IS-A Target Interface

```java
class TypeAAdapter implements UsbCPort { ... }  // IS-A — implements client interface
```

### 5. Always Prefer Constructor Injection

If a class always needs something to work, make it impossible to create that class without it.

```java
// ❌ BAD — creates dependency internally
class TypeAAdapter {
    private TypeAcable cable = new TypeAcable();  // tightly coupled
}

// ✅ GOOD — dependency injected, marked final
class TypeAAdapter {
    private final TypeAcable cable;
    public TypeAAdapter(TypeAcable cable) {  // injected once, reused always
        this.cable = cable;
    }
}
```

### 6. Program to Interface, Not Concrete Class

```java
// ❌ BAD — coupled to specific adapter
private TypeAAdapter cable;

// ✅ GOOD — coupled to interface, any adapter can go here
private UsbCPort cable;
```

### 7. Create Adapters Once, Reuse via Map

```java
// ❌ BAD — new adapter object on every call
new TypeAAdapter(new TypeAcable()).connect("device");

// ✅ GOOD — created once, stored in map, reused
adapterMap.put("typea", new TypeAAdapter(new TypeAcable()));
```

---

## SOLID Principles Covered

| Rule                              | SOLID Principle                     |
| --------------------------------- | ----------------------------------- |
| Never touch legacy code           | Open/Closed Principle               |
| One adapter per legacy class      | Single Responsibility Principle     |
| Adapter HAS-A its own legacy only | Single Responsibility + Composition |
| Program to interface              | Dependency Inversion Principle      |
| Constructor injection             | Dependency Injection                |
| Reuse via Map                     | DRY Principle                       |

---

## When to Use Adapter Pattern?

- You have a **legacy system** you cannot modify
- Two systems have **incompatible interfaces** but need to work together
- You want to **reuse existing code** without rewriting it
- You need a **clean boundary** between old and new code

---

## Two Types of Adapters

| Type               | Mechanism           | Recommended?     |
| ------------------ | ------------------- | ---------------- |
| **Object Adapter** | Composition (HAS-A) | ✅ Always prefer |
| **Class Adapter**  | Inheritance (IS-A)  | ❌ Avoid in Java |

Java does not support multiple class inheritance, so Object Adapter (composition) is always the preferred approach.

---

## Key Takeaway

> **"Your class should know WHAT to do, not WHO does it."**

`Laptop` knows it needs to `connect()` — that is the WHAT.
Whether `TypeAAdapter` or `HdmiAdapter` does it — that is the WHO.
`Laptop` never cares about the WHO. That is why we rely on interfaces.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)

---
