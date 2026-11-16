// How to protect the object using encapsulation and validation.

class ParkingSpot {
    private int spotId;
    private String vehicleNumber;
    private boolean occupied;

    ParkingSpot(int spotId) {
        if (spotId <= 0) {
            throw new IllegalArgumentException(
                "Invalid Parking Spot ID");
        }
        this.spotId = spotId;
        this.occupied = false;
    }

    public void parkVehicle(String vehicleNumber) {
        if (occupied) {
            throw new IllegalStateException(
                "Parking spot is already occupied");
        }
        if (vehicleNumber == null ||
            vehicleNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle number is required");
        }
        this.vehicleNumber = vehicleNumber;
        occupied = true;
    }

    public void display() {
        System.out.println("Spot ID: " + spotId);
        System.out.println("Vehicle: " + vehicleNumber);
        System.out.println("Occupied: " + occupied);
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingSpot p1 = new ParkingSpot(101);
        p1.parkVehicle("TS08AB1234");
        p1.display();
    }
}
