package services;

public class LoyaltyService {
    public void addPoints(String guestName, double amount) {
        int points = (int) amount / 10;
        System.out.println("LoyaltyService → " + points +
                " reward points added for " + guestName);
    }
}
