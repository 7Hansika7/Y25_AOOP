// 2. Multi-field invariants: Relationship between available and vehicleNumber
// Business rule: available <=> vehicleNumber == null

class ParkingSpot {
    private int spotId;
    private String location;
    private boolean available;
    private String vehicleNumber;

    ParkingSpot(int spotId, String location) {
        if (spotId <= 0) {
            throw new IllegalArgumentException(
                "Invalid spot ID"
            );
        }
        this.spotId = spotId;
        this.location = location;
        this.available = true;
        this.vehicleNumber = null;
    }

    void parkVehicle(String vehicleNumber) {
        if (!available) {
            throw new IllegalStateException(
                "Parking spot is already occupied"
            );
        }
        if (vehicleNumber == null ||
            vehicleNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle number cannot be empty"
            );
        }
        this.vehicleNumber = vehicleNumber;
        this.available = false;
    }

    void displayDetails() {
        System.out.println("Spot ID: " + spotId);
        System.out.println("Location: " + location);
        System.out.println("Available: " + available);
        System.out.println("Vehicle: " + vehicleNumber);
    }

    public static void main(String[] args) {
        ParkingSpot p1 =
            new ParkingSpot(101, "Hyderabad");
        p1.parkVehicle("TS09AB1234");
        p1.displayDetails();
    }
}
