// 2. Tell, Don't Ask
// Instead of asking an object for its data and making decisions outside it, tell the object what action to perform.

class ParkingSpot {
    private boolean available = true;
    private String vehicleNumber = null;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Tell: ParkingSpot checks its own state and executes operation safely
    public void parkVehicle(String vehicleNumber) {
        if (!available) {
            throw new IllegalStateException("Parking spot is already occupied");
        }
        this.vehicleNumber = vehicleNumber;
        this.available = false;
        System.out.println("Vehicle parked: " + vehicleNumber);
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingSpot spot = new ParkingSpot();

        // Incorrect approach – Ask:
        // Here, the external class checks the state and modifies it.
        /*
        if (spot.isAvailable()) {
            spot.setAvailable(false);
        }
        */

        // Better approach – Tell:
        spot.parkVehicle("TS09AB1234");
    }
}
