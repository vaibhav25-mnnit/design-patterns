import commands.Command;
import commands.MacroCommand;
import commands.ac.AcOffCommand;
import commands.ac.AcOnCommand;
import commands.ac.AcTemperatureCommand;
import commands.fan.FanDecreaseSpeedCommand;
import commands.fan.FanIncreaseSpeedCommand;
import commands.fan.FanOffCommand;
import commands.fan.FanOnCommand;
import commands.light.LightOffCommand;
import commands.light.LightOnCommand;
import devices.AirConditioner;
import devices.Fan;
import devices.Light;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!\nThis is a Remote Controller for command pattern\n");

        // ── create receivers ──────────────────────────────
        Light livingRoomLight = new Light("Living Room");
        Light          bedroomLight    = new Light("Bedroom");
        Fan ceilingFan      = new Fan();
        AirConditioner ac              = new AirConditioner();

        // ── create commands ───────────────────────────────
        // Light commands
        Command livingLightOn  = new LightOnCommand(livingRoomLight);
        Command livingLightOff = new LightOffCommand(livingRoomLight);
        Command bedLightOn     = new LightOnCommand(bedroomLight);
        Command bedLightOff    = new LightOffCommand(bedroomLight);

        // Fan commands
        // fanOn  → resumes at lastActiveSpeed (real life behaviour)
        // fanOff → turns off, remembers last speed
        // fanIncrease / fanDecrease → one step at a time
        Command fanOn       = new FanOnCommand(ceilingFan);
        Command fanOff      = new FanOffCommand(ceilingFan);
        Command fanIncrease = new FanIncreaseSpeedCommand(ceilingFan);
        Command fanDecrease = new FanDecreaseSpeedCommand(ceilingFan);

        // AC commands
        Command acOn      = new AcOnCommand(ac);
        Command acOff     = new AcOffCommand(ac);
        Command acSetTemp = new AcTemperatureCommand(ac, 18);

        // ── assign commands to remote slots ───────────────
        RemoteControl remote = new RemoteControl();
        remote.setCommand(0, livingLightOn,  "Living Room Light ON",  livingLightOff, "Living Room Light OFF");
        remote.setCommand(1, bedLightOn,     "Bedroom Light ON",      bedLightOff,    "Bedroom Light OFF"    );
        remote.setCommand(2, fanOn,          "Fan ON",                fanOff,         "Fan OFF"              );
        remote.setCommand(3, fanIncrease,    "Fan Speed UP",          fanDecrease,    "Fan Speed DOWN"       );
        remote.setCommand(4, acOn,           "AC ON",                 acOff,          "AC OFF"               );
        remote.setCommand(5, acSetTemp,      "AC Temp → 18°C",        acOff,          "AC OFF"               );
        // ── test fan on/off with speed memory ────────────
        System.out.println("========== Fan ON/OFF with Speed Memory ==========");
        remote.pressOn(3);    // OFF    → LOW    (increase)
        remote.pressOn(3);    // LOW    → MEDIUM (increase)
        remote.pressOn(3);    // MEDIUM → HIGH   (increase, lastActiveSpeed = HIGH)
        remote.pressOff(2);   // fan OFF         (lastActiveSpeed still = HIGH)
        remote.pressOn(2);    // fan ON           → resumes at HIGH (real life!)
        remote.pressUndo();   // undo fanOn       → back to OFF
        remote.pressUndo();   // undo fanOff      → back to HIGH

        // print remote layout before any action
        remote.printRemote();

        // ── test fan increase/decrease with undo ─────────
        System.out.println("\n========== Fan Speed Increase/Decrease ==========");
        remote.pressOff(2);   // fan OFF
        remote.pressOn(3);    // OFF    → LOW
        remote.pressOn(3);    // LOW    → MEDIUM
        remote.pressOn(3);    // MEDIUM → HIGH
        remote.pressOn(3);    // already MAX — no change
        remote.pressUndo();   // undo increase → back to MEDIUM
        remote.pressOff(3);   // MEDIUM → LOW  (decrease)
        remote.pressOff(3);   // LOW    → OFF  (decrease)
        remote.pressOff(3);   // already MIN  — no change
        remote.pressUndo();   // undo decrease → back to LOW

        // ── test AC commands ──────────────────────────────
        System.out.println("\n========== AC Commands ==========");
        remote.pressOn(4);    // AC ON  at 24°C (default)
        remote.pressOn(5);    // AC set to 18°C
        remote.pressUndo();   // undo temp change → back to 24°C
        remote.pressUndo();   // undo AC on → AC OFF

        // ── test macro command ────────────────────────────
        System.out.println("\n========== Good Night Macro ==========");
        // good night = all lights off + fan off + AC off
        Command[] goodNight = { livingLightOff, bedLightOff, fanOff, acOff };
        MacroCommand goodNightMacro = new MacroCommand(goodNight);
        remote.setCommand(0, goodNightMacro, "Good Night (ALL OFF)", goodNightMacro, "Good Night (ALL OFF)");

        // first turn things on so macro has something to turn off
        livingRoomLight.on();
        bedroomLight.on();
        ac.on();

        remote.pressOn(0);    // good night — turns everything off
        System.out.println("\n--- Undo Good Night ---");
        remote.pressUndo();   // undo all in reverse order

        // ── test empty slot ───────────────────────────────
        System.out.println("\n========== Empty Slot ==========");
        RemoteControl freshRemote = new RemoteControl();
        freshRemote.pressOn(3);   // no command assigned → NoCommand fires
        freshRemote.pressUndo();  // nothing in history → graceful message
    }
}