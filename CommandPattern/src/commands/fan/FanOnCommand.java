package commands.fan;

import commands.Command;
import devices.Fan;

public class FanOnCommand implements Command {
    private final Fan fan;
    private Fan.Speed previousSpeed;  // saved at execute() time

    public FanOnCommand(Fan fan) { this.fan = fan; }

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();
        fan.setSpeed(fan.getLastActiveSpeed()); // resume at last known speed
    }

    @Override
    public void undo() { fan.setSpeed(previousSpeed); }
}