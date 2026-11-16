// Doorway 1: Public setters allow invalid values to be assigned directly

class ParkingSpot {
    private int spotId;

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public int getSpotId() {
        return spotId;
    }
}

public class Main {
    public static void main(String[] args) {
        // Problem:
        ParkingSpot p1 = new ParkingSpot();
        p1.setSpotId(-101); // Public setter bypasses constructor validation
        System.out.println("Spot ID set via public setter: " + p1.getSpotId());
        // Solution: Avoid unnecessary setters. Use constructors and validated methods.
    }
}
