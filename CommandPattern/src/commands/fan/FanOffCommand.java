package commands.fan;

import commands.Command;
import devices.Fan;

public class FanOffCommand implements Command {
    private final Fan fan;
    private Fan.Speed previousSpeed;  // saved at execute() time

    public FanOffCommand(Fan fan) { this.fan = fan; }

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();
        fan.setSpeed(Fan.Speed.OFF);
    }

    @Override
    public void undo() { fan.setSpeed(previousSpeed); }
}