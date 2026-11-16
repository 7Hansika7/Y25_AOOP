// Single-field invariants: Unprotected field allows invalid values (e.g., negative ID)

class ParkingSpot {
    int spotId;

    public static void main(String[] args) {
        ParkingSpot p1 = new ParkingSpot();
        p1.spotId = -101;
        System.out.println(p1.spotId);
    }
}
