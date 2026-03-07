package commands.fan;

import commands.Command;
import devices.Fan;

public class FanIncreaseSpeedCommand implements Command {
    private final Fan fan;
    private Fan.Speed previousSpeed;  // saved at execute() time

    public FanIncreaseSpeedCommand(Fan fan) { this.fan = fan; }

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();
        Fan.Speed[] speeds = Fan.Speed.values();
        int current = fan.getSpeed().ordinal();
        if (current < speeds.length - 1) {
            fan.setSpeed(speeds[current + 1]);
        } else {
            System.out.println("Fan → Already at MAX speed (HIGH)");
        }
    }

    @Override
    public void undo() { fan.setSpeed(previousSpeed); }
}