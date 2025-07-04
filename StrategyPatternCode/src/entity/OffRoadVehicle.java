package entity;

import Stratigies.Drive.OffRoadDriveStrategy;

public class OffRoadVehicle extends Vehicle{

    public OffRoadVehicle(){
        super(new OffRoadDriveStrategy());
    }
}
