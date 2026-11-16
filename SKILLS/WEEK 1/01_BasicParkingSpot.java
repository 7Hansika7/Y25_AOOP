// Problem: Create a parking spot in the Waypoint application.

class ParkingSpot {
    int spotId;
    String location;
    boolean available;

    void displayDetails() {
        System.out.println("Spot ID: " + spotId);
        System.out.println("Location: " + location);
        System.out.println("Available: " + available);
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingSpot p1 = new ParkingSpot();
        p1.spotId = 101;
        p1.location = "Hyderabad";
        p1.available = true;
        p1.displayDetails();
    }
}
