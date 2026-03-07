package devices;

public class AirConditioner {
    private boolean isOn       = false;
    private int     temperature = 24;  // default temp

    public void on()  {
        isOn = true;
        System.out.println("AC → Turned ON at " + temperature + "°C");
    }

    public void off() {
        isOn = false;
        System.out.println("AC → Turned OFF");
    }

    public void setTemperature(int temp) {
        this.temperature = temp;
        System.out.println("AC → Temperature set to " + temp + "°C");
    }

    public int getTemperature() { return temperature; }
    public boolean isOn()       { return isOn; }
}
