package Adapters;

import commonPort.UsbCPort;
import legacy.HdmiCable;

public class HdmiAdapter implements UsbCPort {
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
