package entity;

import Stratigies.Drive.SportsDriveStrategy;

public class SportzVehicle extends Vehicle{

    public SportzVehicle(){
        super(new SportsDriveStrategy());
    }
}
