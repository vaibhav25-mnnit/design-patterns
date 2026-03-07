package commands.ac;

import commands.Command;
import devices.AirConditioner;

public class AcOnCommand implements Command {
    private final AirConditioner ac;

    public AcOnCommand(AirConditioner ac) { this.ac = ac; }

    @Override public void execute() { ac.on();  }
    @Override public void undo()    { ac.off(); }
}
