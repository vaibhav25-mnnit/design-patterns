package services;

public class RoomService {
    public boolean checkAvailability(String roomType) {
        System.out.println("RoomService    → Checking availability for: " + roomType);
        return true; // assume available
    }

    public void reserveRoom(String guestName, String roomType, int nights) {
        System.out.println("RoomService    → Room reserved for " + guestName +
                " | " + roomType + " | " + nights + " nights");
    }
}
