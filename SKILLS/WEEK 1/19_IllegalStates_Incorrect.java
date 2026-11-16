// Modelling I: 3. Illegal States - Incorrect Implementation
// Allows invalid combination (available=true, yet vehicleNumber is assigned)

class ParkingSpot {
    boolean available;
    String vehicleNumber;
}

public class Main {
    public static void main(String[] args) {
        ParkingSpot p1 = new ParkingSpot();
        p1.available = true;
        p1.vehicleNumber = "TS09AB1234";

        // Illegal state: Spot is marked available even though a vehicle is assigned!
        System.out.println("Available: " + p1.available + ", Vehicle: " + p1.vehicleNumber);
    }
}
