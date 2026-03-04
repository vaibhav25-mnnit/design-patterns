package Adapters;

import commonPort.UsbCPort;
import legacy.TypeBcable;

public class TypeBAdapter implements UsbCPort {
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
