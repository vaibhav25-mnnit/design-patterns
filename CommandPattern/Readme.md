# Command Design Pattern in Java
> Interview-focused revision guide — Remote Controller (Light + Fan + AC) with Undo.

---

## One Line
> **Encapsulate a request as an object** so you can queue, log, and undo operations — decoupling the object that invokes from the object that executes.

---

## Analogy
```
Waiter        → Invoker     (takes order, never cooks)
Order slip    → Command     (what needs to be done)
Chef          → Receiver    (actually cooks)
Customer      → Client      (decides what to order)
```

---

## 4 Participants — Roles Clearly

| Participant | Role | In This Code |
|---|---|---|
| **Receiver** | Actual business logic | `Light`, `Fan`, `AirConditioner` |
| **Command** | Wraps receiver + execute/undo | `LightOnCommand`, `FanOnCommand`... |
| **Invoker** | Fires commands, owns history stack | `RemoteControl` |
| **Client** | Wires everything together | `main()` |

---

## Command Interface
```java
interface Command {
    void execute();  // mandatory always
    void undo();     // add only if undo is needed
}
```
> `execute()` is the only mandatory method. Add `undo()`, `canExecute()`, `getDescription()` only when your use case needs them — don't over-engineer.

---

## Receivers — Never Touched by Invoker
```java
class Light {
    public void on()  { System.out.println(location + " Light → ON");  }
    public void off() { System.out.println(location + " Light → OFF"); }
}

class Fan {
    public enum Speed { OFF, LOW, MEDIUM, HIGH }
    private Speed currentSpeed    = Speed.OFF;
    private Speed previousSpeed   = Speed.OFF;
    private Speed lastActiveSpeed = Speed.LOW;  // remembers last non-OFF speed

    public void setSpeed(Speed speed) {
        previousSpeed = currentSpeed;
        if (speed != Speed.OFF) lastActiveSpeed = speed; // OFF never erases memory
        currentSpeed = speed;
    }
}
```
> `lastActiveSpeed` lives in `Fan` (Receiver) — not in Command. Device behaviour belongs to the device.

---

## Concrete Commands — Critical Rule ⚠️
```java
// previousSpeed saved at execute() time — NEVER at constructor
class FanOnCommand implements Command {
    private final Fan fan;
    private Fan.Speed previousSpeed; // saved when button is pressed

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();          // ← save HERE not in constructor
        fan.setSpeed(fan.getLastActiveSpeed());  // resume at last known speed
    }

    @Override public void undo() { fan.setSpeed(previousSpeed); }
}

// increase one step: OFF → LOW → MEDIUM → HIGH
class FanIncreaseSpeedCommand implements Command {
    private Fan.Speed previousSpeed;

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();
        int current = fan.getSpeed().ordinal();
        Fan.Speed[] speeds = Fan.Speed.values();
        if (current < speeds.length - 1) fan.setSpeed(speeds[current + 1]);
        else System.out.println("Fan → Already at MAX");
    }

    @Override public void undo() { fan.setSpeed(previousSpeed); }
}
```

> **Interview trap** — if you save `previousSpeed` in the constructor, undo will always restore to the state at object creation, not the state just before the button was pressed.

---

## Invoker — Owns History, Knows Nothing About Devices
```java
class RemoteControl {
    private final Command[] onButtons;
    private final Command[] offButtons;
    private final Stack<Command> history = new Stack<>(); // only invoker owns this

    public void pressOn(int slot) {
        onButtons[slot].execute();
        history.push(onButtons[slot]); // every action tracked
    }

    public void pressUndo() {
        if (!history.isEmpty()) history.pop().undo(); // pop and reverse
    }
}
```

---

## MacroCommand — Multiple Commands as One
```java
class MacroCommand implements Command {
    private final Command[] commands;

    @Override
    public void execute() {
        for (Command c : commands) c.execute();
    }

    @Override
    public void undo() {
        // reverse order is critical for undo
        for (int i = commands.length - 1; i >= 0; i--) commands[i].undo();
    }
}

// usage — Good Night scene
Command[] goodNight = { livingLightOff, bedLightOff, fanOff, acOff };
remote.pressOn(0); // fires all 4 — one button
remote.pressUndo();// undoes all 4 in reverse
```

---

## NoCommand — Null Object Pattern Inside Command Pattern
```java
// empty slots use this instead of null — eliminates null checks in invoker
class NoCommand implements Command {
    @Override public void execute() { System.out.println("No command assigned"); }
    @Override public void undo()    { System.out.println("Nothing to undo");     }
}
```
> Interviewers love this — shows you know Null Object Pattern too.

---

## Slot Layout (printed at runtime)
```
┌─────────────────────────────────────────────────────┐
│               REMOTE CONTROL LAYOUT                 │
├──────┬──────────────────────────┬────────────────────┤
│ SLOT │        ON Button         │    OFF Button      │
├──────┼──────────────────────────┼────────────────────┤
│  0   │ Living Room Light ON     │ Living Room Light  │
│  1   │ Bedroom Light ON         │ Bedroom Light OFF  │
│  2   │ Fan ON                   │ Fan OFF            │
│  3   │ Fan Speed UP             │ Fan Speed DOWN     │
│  4   │ AC ON                    │ AC OFF             │
│  5   │ AC Temp → 18°C           │ AC OFF             │
├──────┴──────────────────────────┴────────────────────┤
│                  [UNDO] last action                  │
└───────────────────────────────────────────────────────┘
```

---

## Responsibility — Who Owns What

```
Receiver   → HOW to perform action       (Fan knows how to set speed)
Command    → WHAT action + how to undo   (FanOnCommand knows to resume lastActiveSpeed)
Invoker    → WHEN to fire + history      (RemoteControl owns Stack<Command>)
Client     → WHO gets wired to WHAT      (main() creates and connects everything)
```

---

## Interview Questions

**Q1. What does Command Pattern decouple?**
```
The object that INVOKES an operation (Invoker)
from the object that KNOWS how to perform it (Receiver).
Command is the bridge.
```

**Q2. How does undo work?**
```
Every execute() pushes command onto a Stack in the Invoker.
pressUndo() pops and calls undo() on it.
Each command knows how to reverse itself.
```

**Q3. Why save previousSpeed in execute() not constructor?**
```
Constructor runs at object creation — fan may be at OFF.
execute() runs when button is pressed — fan is at actual current speed.
Saving in constructor means undo always goes back to OFF, not to the
real previous state. Always save state at execute() time.
```

**Q4. Command vs Strategy — most asked**

| | Command | Strategy |
|---|---|---|
| **Encapsulates** | A request / action | An algorithm / behaviour |
| **Has undo?** | ✅ Yes | ❌ No |
| **State** | Holds previousState for undo | Usually stateless |
| **Example** | Remote buttons | Sorting algorithms |

**Q5. Command vs Memento for undo — when to use which?**
```
Command  → stores the ACTION and reverses it programmatically
           use when: each action can be mathematically reversed
           e.g. light.on() → undo = light.off()

Memento  → stores a SNAPSHOT of entire state, restores directly
           use when: state is complex, reversal logic is hard
           e.g. text editor Ctrl+S save points

Real editors use BOTH:
Command → undo/redo keystroke by keystroke
Memento → version history / save points
```

**Q6. Where is Command Pattern in Java?**
```
java.lang.Runnable     → IS the Command pattern
                         run()   = execute()
                         Thread  = Invoker
                         Your code = Receiver

ThreadPoolExecutor     → Invoker queuing and executing Runnables
javax.swing.Action     → UI button commands
Git commits            → each commit is a command, revert = undo
DB transactions        → each operation is command, rollback = undo
```

**Q7. Does Command interface always need undo()?**
```
No. Only add undo() when your use case needs it.
For fire-and-forget tasks (thread pool, button click) — execute() only.
For editors, remote controls, transactions — execute() + undo().
```

---

## When to Use
- Need **undo/redo**
- Need to **queue or schedule** operations
- Need **audit log** of operations
- Need to **parameterize** objects with actions

## When NOT to Use
- Simple direct method calls with no undo/queue needed — overkill
- Too many commands → class explosion — consider using lambdas instead

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)