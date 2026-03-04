package Adapters;

import commonPort.UsbCPort;
import legacy.TypeAcable;

public class TypeAAdapter implements UsbCPort {
    private final TypeAcable typeAcable;           // HAS-A (composition)

    public TypeAAdapter(TypeAcable typeAcable) {   // injected, not created
        this.typeAcable = typeAcable;
    }

    @Override
    public void connect(String deviceName) {

    }
}
