// Modelling I: 3. Illegal States - Better Implementation
// The vehicle number cannot be modified directly.
// An occupied spot cannot accept another vehicle.
// The system controls the state transitions.

class ParkingSpot {
    private String vehicleNumber;

    public void parkVehicle(String number) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle number is required");
        }
        if (vehicleNumber != null) {
            throw new IllegalStateException(
                "Parking spot is already occupied");
        }
        vehicleNumber = number;
    }

    public void releaseSpot() {
        if (vehicleNumber == null) {
            throw new IllegalStateException(
                "Parking spot is already available");
        }
        vehicleNumber = null;
    }

    public boolean isAvailable() {
        return vehicleNumber == null;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingSpot spot = new ParkingSpot();
        System.out.println("Initially available: " + spot.isAvailable());

        spot.parkVehicle("TS09AB1234");
        System.out.println("Available after parking: " + spot.isAvailable() + ", Vehicle: " + spot.getVehicleNumber());

        spot.releaseSpot();
        System.out.println("Available after release: " + spot.isAvailable());
    }
}
