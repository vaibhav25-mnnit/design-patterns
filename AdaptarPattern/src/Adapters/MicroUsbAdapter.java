package Adapters;

import commonPort.UsbCPort;
import legacy.MicroUsbCable;

public class MicroUsbAdapter implements UsbCPort {
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
