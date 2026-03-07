import commands.Command;
import commands.NoCommand;

import java.util.Stack;

public class RemoteControl {
    private static final int SLOTS = 6;  // added one more slot for fan increase/decrease

    private final Command[] onButtons;   // on button per slot
    private final Command[] offButtons;  // off button per slot
    private final String[]  onLabels;     // label for on button — for printing
    private final String[]  offLabels;    // label for off button — for printing
    private final Stack<Command> history = new Stack<>(); // for undo

    public RemoteControl() {
        onButtons  = new Command[SLOTS];
        offButtons = new Command[SLOTS];
        onLabels   = new String[SLOTS];
        offLabels  = new String[SLOTS];
        // fill all slots with NoCommand — no null checks needed
        for (int i = 0; i < SLOTS; i++) {
            onButtons[i]  = new NoCommand();
            offButtons[i] = new NoCommand();
            onLabels[i]   = "--- empty ---";
            offLabels[i]  = "--- empty ---";
        }
    }

    // assign commands to a slot
    public void setCommand(int slot, Command onCommand, String onLabel,
                           Command offCommand, String offLabel) {
        onButtons[slot]  = onCommand;
        offButtons[slot] = offCommand;
        onLabels[slot]   = onLabel;
        offLabels[slot]  = offLabel;
    }

    // press ON button of a slot
    public void pressOn(int slot) {
        System.out.print("Button ON  [slot " + slot + "] → ");
        onButtons[slot].execute();
        history.push(onButtons[slot]);
    }

    // press OFF button of a slot
    public void pressOff(int slot) {
        System.out.print("Button OFF [slot " + slot + "] → ");
        offButtons[slot].execute();
        history.push(offButtons[slot]);
    }

    // undo last pressed button
    public void pressUndo() {
        if (history.isEmpty()) {
            System.out.println("Undo → Nothing to undo");
            return;
        }
        System.out.print("Undo → ");
        history.pop().undo();
    }

    // prints the full remote layout — call this after wiring all commands
    public void printRemote() {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│               REMOTE CONTROL LAYOUT                 │");
        System.out.println("├──────┬──────────────────────────┬────────────────────┤");
        System.out.println("│ SLOT │        ON Button         │    OFF Button      │");
        System.out.println("├──────┼──────────────────────────┼────────────────────┤");
        for (int i = 0; i < SLOTS; i++) {
            System.out.printf("│  %d   │ %-24s │ %-18s │%n", i, onLabels[i], offLabels[i]);
        }
        System.out.println("├──────┴──────────────────────────┴────────────────────┤");
        System.out.println("│                  [UNDO] last action                  │");
        System.out.println("└───────────────────────────────────────────────────────┘");
        System.out.println();
    }
}
