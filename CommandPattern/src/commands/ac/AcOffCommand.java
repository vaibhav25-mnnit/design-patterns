package commands.ac;

import commands.Command;
import devices.AirConditioner;

public class AcOffCommand implements Command {
    private final AirConditioner ac;

    public AcOffCommand(AirConditioner ac) { this.ac = ac; }

    @Override public void execute() { ac.off(); }
    @Override public void undo()    { ac.on();  }
}
