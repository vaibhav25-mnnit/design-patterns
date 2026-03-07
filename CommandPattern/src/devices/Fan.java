package devices;

public class Fan {
    public enum Speed { OFF, LOW, MEDIUM, HIGH }

    private Speed currentSpeed  = Speed.OFF;
    private Speed previousSpeed = Speed.OFF;  // for undo
    private Speed lastActiveSpeed = Speed.LOW; // remembers last non-OFF speed

    public void setSpeed(Speed speed) {
        previousSpeed = currentSpeed;

        // only update lastActiveSpeed when setting a non-OFF speed
        if (speed != Speed.OFF) {
            lastActiveSpeed = speed;
        }

        currentSpeed = speed;
        System.out.println("Fan → Speed set to: " + speed);
    }

    public Speed getSpeed()          { return currentSpeed;     }
    public Speed getPreviousSpeed()  { return previousSpeed;    }
    public Speed getLastActiveSpeed(){ return lastActiveSpeed;  }
}