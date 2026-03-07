package commands;

// ============================================================
// NO-OP COMMAND — used for empty slots on remote
// avoids null checks in RemoteControl
// This is the Null Object Pattern used inside Command Pattern
// ============================================================
public class NoCommand implements Command {
    @Override public void execute() { System.out.println("No command assigned to this slot"); }
    @Override public void undo()    { System.out.println("Nothing to undo"); }
}