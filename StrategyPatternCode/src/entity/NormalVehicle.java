package entity;

import Stratigies.Drive.DriveStrategy;
import Stratigies.Drive.NormalDriveStrategy;

public class NormalVehicle extends Vehicle{
    public NormalVehicle() {
        super(new NormalDriveStrategy());
    }
}
