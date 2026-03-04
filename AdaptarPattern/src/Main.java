import Adapters.HdmiAdapter;
import Adapters.MicroUsbAdapter;
import Adapters.TypeAAdapter;
import Adapters.TypeBAdapter;
import commonPort.UsbCPort;
import legacy.HdmiCable;
import legacy.MicroUsbCable;
import legacy.TypeAcable;
import legacy.TypeBcable;
import entity.Laptop;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Step 1: Create adaptees ONCE (the actual cable handlers)
        TypeAcable typeA    = new TypeAcable();
        TypeBcable typeB    = new TypeBcable();
        MicroUsbCable microUsb = new MicroUsbCable();
        HdmiCable hdmi     = new HdmiCable();

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