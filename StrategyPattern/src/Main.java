import entity.NormalVehicle;
import entity.OffRoadVehicle;
import entity.SportzVehicle;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hi there this is a strategy pattern code example");

        NormalVehicle normalVehicle = new NormalVehicle();
        OffRoadVehicle offRoadVehicle = new OffRoadVehicle();
        SportzVehicle sportzVehicle = new SportzVehicle();


        normalVehicle.drive();
        offRoadVehicle.drive();
        sportzVehicle.drive();

    }
}