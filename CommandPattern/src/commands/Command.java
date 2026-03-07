package commands;

// ============================================================
// COMMAND INTERFACE — every command must implement this
// execute() → do the action
// undo()    → reverse the action
// ===============================
public interface Command {
    void execute();
    void undo();
}
