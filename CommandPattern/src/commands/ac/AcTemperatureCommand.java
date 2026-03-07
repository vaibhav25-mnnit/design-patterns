package commands.ac;

import commands.Command;
import devices.AirConditioner;

public class AcTemperatureCommand  implements Command {
    private final AirConditioner ac;
    private final int newTemp;
    private int previousTemp;  // stored at execute time for undo

    public AcTemperatureCommand(AirConditioner ac, int newTemp) {
        this.ac      = ac;
        this.newTemp = newTemp;
    }

    @Override
    public void execute() {
        previousTemp = ac.getTemperature();  // save current before changing
        ac.setTemperature(newTemp);
    }

    @Override
    public void undo() {
        ac.setTemperature(previousTemp);     // restore saved temperature
    }
}