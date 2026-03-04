package entity;
import commonPort.UsbCPort;
import java.util.Map;

public class Laptop implements UsbCPort {
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

    @Override
    // overload so Laptop itself can be used as UsbCPort
    public void connect(String deviceName) {
        System.out.println("USB-C Native port → Connected: " + deviceName);
    }
}
