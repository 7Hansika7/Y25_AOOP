// Doorway 5: Equality that lies
// Solution: Define equality based on the object's business identity using equals() and hashCode()

class ParkingSpot {
    private int spotId;
    private String location;

    ParkingSpot(int spotId, String location) {
        this.spotId = spotId;
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ParkingSpot)) return false;
        ParkingSpot other = (ParkingSpot) obj;
        return this.spotId == other.spotId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(spotId);
    }

    public int getSpotId() {
        return spotId;
    }

    public String getLocation() {
        return location;
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingSpot p1 = new ParkingSpot(101, "Hyderabad");
        ParkingSpot p2 = new ParkingSpot(102, "Hyderabad");
        ParkingSpot p3 = new ParkingSpot(101, "Secunderabad");

        // Two spots with same location but different IDs are distinct entities:
        System.out.println("p1.equals(p2): " + p1.equals(p2)); // false

        // Two spots with same spotId represent the same business entity:
        System.out.println("p1.equals(p3): " + p1.equals(p3)); // true
    }
}
