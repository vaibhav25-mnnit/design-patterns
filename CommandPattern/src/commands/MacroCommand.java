package commands;

// ============================================================
// MACRO COMMAND — executes a LIST of commands together
// Perfect for "Good Morning" / "Good Night" scenes
// undo() reverses all commands in reverse order
// ============================================================
public class MacroCommand  implements Command {
    private final Command[] commands;

    public MacroCommand(Command[] commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        System.out.println("--- Macro: executing all commands ---");
        for (Command c : commands) c.execute();
    }

    @Override
    public void undo() {
        System.out.println("--- Macro: undoing all commands in reverse ---");
        for (int i = commands.length - 1; i >= 0; i--) commands[i].undo();
    }
}