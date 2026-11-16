// Doorway 2: Escaping references
// An object exposes a reference to its internal mutable data, allowing external modifications.

import java.util.ArrayList;
import java.util.List;

// Vulnerable class
class BookingVulnerable {
    private List<String> vehicleNumbers = new ArrayList<>();

    public List<String> getVehicleNumbers() {
        return vehicleNumbers; // Escapes reference!
    }
}

// Protected class using defensive copying
class BookingProtected {
    private List<String> vehicleNumbers = new ArrayList<>();

    public BookingProtected(List<String> initialVehicles) {
        this.vehicleNumbers.addAll(initialVehicles);
    }

    // Solution: Return a defensive copy or an unmodifiable view.
    public List<String> getVehicleNumbers() {
        return List.copyOf(vehicleNumbers);
    }
}

public class Main {
    public static void main(String[] args) {
        // Demonstration of vulnerability:
        BookingVulnerable booking = new BookingVulnerable();
        booking.getVehicleNumbers().add("TS09AB1234");
        System.out.println("Before external modification: " + booking.getVehicleNumbers());

        // External class directly modifies internal state:
        booking.getVehicleNumbers().clear();
        System.out.println("After booking.getVehicleNumbers().clear(): " + booking.getVehicleNumbers());

        // Demonstration of solution:
        BookingProtected safeBooking = new BookingProtected(List.of("TS09AB1234"));
        System.out.println("Safe booking list: " + safeBooking.getVehicleNumbers());
        // safeBooking.getVehicleNumbers().clear(); // Throws UnsupportedOperationException
    }
}
