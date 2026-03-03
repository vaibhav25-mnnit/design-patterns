import java.util.HashMap;
import java.util.Map;

// ============================================================
// REAL WORLD ANALOGY:
// Your laptop has a USB-C port (Target Interface).
// You have old devices with TypeA, MicroUSB, HDMI ports (Adaptees).
// You need an Adapter (dongle/converter) to connect them.
// ============================================================


// ============================================================
// TARGET INTERFACE — what our Laptop (client) understands
// All cables must go through USB-C port
// ============================================================
interface UsbCPort {
    void connect(String deviceName);
}


// ============================================================
// ADAPTEES — Legacy cable systems (incompatible with USB-C)
// These already exist, we cannot modify them
// ============================================================

class TypeAcable {
    public void connectViaTypeA(String deviceName) {
        System.out.println("[Type-A]    Connected: " + deviceName);
    }
}

class TypeBcable {
    public void connectViaTypeB(String deviceName) {
        System.out.println("[Type-B]    Connected: " + deviceName);
    }
}

class MicroUsbCable {
    public void connectViaMicroUsb(String deviceName) {
        System.out.println("[Micro-USB] Connected: " + deviceName);
    }
}

class HdmiCable {
    public void connectViaHdmi(String deviceName) {
        System.out.println("[HDMI]      Connected: " + deviceName);
    }
}


// ============================================================
// ADAPTERS — Dongles that convert each cable to USB-C
// Each adapter:
//   IS-A  UsbCPort      (implements target interface)
//   HAS-A <CableType>   (injected via constructor — not created internally)
// ============================================================

class TypeAAdapter implements UsbCPort {
    private final TypeAcable typeAcable;           // HAS-A (composition)

    public TypeAAdapter(TypeAcable typeAcable) {   // injected, not created
        this.typeAcable = typeAcable;
    }

    @Override
    public void connect(String deviceName) {
        System.out.print("USB-C → Type-A Adapter → ");
        typeAcable.connectViaTypeA(deviceName);    // delegates to adaptee
    }
}

class TypeBAdapter implements UsbCPort {
    private final TypeBcable typeBcable;

    public TypeBAdapter(TypeBcable typeBcable) {
        this.typeBcable = typeBcable;
    }

    @Override
    public void connect(String deviceName) {
        System.out.print("USB-C → Type-B Adapter → ");
        typeBcable.connectViaTypeB(deviceName);
    }
}

class MicroUsbAdapter implements UsbCPort {
    private final MicroUsbCable microUsbCable;

    public MicroUsbAdapter(MicroUsbCable microUsbCable) {
        this.microUsbCable = microUsbCable;
    }

    @Override
    public void connect(String deviceName) {
        System.out.print("USB-C → Micro-USB Adapter → ");
        microUsbCable.connectViaMicroUsb(deviceName);
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


// ============================================================
// LAPTOP (Concrete Client)
// - Receives a Map of adapters via constructor (not created internally)
// - Map ensures adapters are created ONCE and reused (not recreated each call)
// - Holds reference as UsbCPort (interface), not concrete adapter class
// ============================================================

class Laptop implements UsbCPort {
    private final Map<String, UsbCPort> adapterMap;  // HAS-A map of adapters

    public Laptop(Map<String, UsbCPort> adapterMap) { // injected via constructor
        this.adapterMap = adapterMap;
    }
    
    public void connect(String cableType, String deviceName) {
        // Native USB-C — no adapter needed
        if (cableType.equalsIgnoreCase("usbc")) {
            System.out.println("USB-C      Native port → Connected: " + deviceName);
            return;
        }

        // Lookup adapter from map — O(1), no new object created
        UsbCPort adapter = adapterMap.get(cableType.toLowerCase());

        if (adapter != null) {
            adapter.connect(deviceName);
        } else {
            System.out.println("No adapter found for cable type: " + cableType);
        }
    }

    // overload so Laptop itself can be used as UsbCPort
    @Override
    public void connect(String deviceName) {
        System.out.println("USB-C Native port → Connected: " + deviceName);
    }
}


// ============================================================
// MAIN — Wire everything together
// Adapters are created ONCE here and injected — never recreated
// ============================================================

public class AdapterPattern {
    public static void main(String[] args) {

        // Step 1: Create adaptees ONCE (the actual cable handlers)
        TypeAcable    typeA    = new TypeAcable();
        TypeBcable    typeB    = new TypeBcable();
        MicroUsbCable microUsb = new MicroUsbCable();
        HdmiCable     hdmi     = new HdmiCable();

        // Step 2: Wrap each adaptee in its adapter ONCE
        // Each adapter IS-A UsbCPort and HAS-A specific cable
        Map<String, UsbCPort> adapterMap = new HashMap<>();
        adapterMap.put("typea",    new TypeAAdapter(typeA));
        adapterMap.put("typeb",    new TypeBAdapter(typeB));
        adapterMap.put("microusb", new MicroUsbAdapter(microUsb));
        adapterMap.put("hdmi",     new HdmiAdapter(hdmi));
        // Adding a new cable tomorrow? Just add one line here. Laptop class unchanged.

        // Step 3: Inject the map into Laptop — Laptop never creates adapters itself
        Laptop laptop = new Laptop(adapterMap);

        // Step 4: Connect various devices — adapters are REUSED, not recreated
        System.out.println("======= Connecting Devices =======");
        laptop.connect("usbc",     "Samsung SSD");
        laptop.connect("typea",    "Old USB Drive");
        laptop.connect("typea",    "Keyboard");        // reuses same TypeA adapter
        laptop.connect("typeb",    "External HDD");
        laptop.connect("microusb", "Old Android Phone");
        laptop.connect("microusb", "Bluetooth Speaker"); // reuses same MicroUSB adapter
        laptop.connect("hdmi",     "External Monitor");
        laptop.connect("vga",      "Old Projector");   // no adapter → graceful message
    }
}