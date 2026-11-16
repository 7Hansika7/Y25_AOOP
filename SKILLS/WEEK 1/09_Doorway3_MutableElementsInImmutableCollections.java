// Doorway 3: Mutable elements inside immutable collections
// An unmodifiable collection does NOT guarantee that the elements within it cannot change!

import java.util.List;

class Vehicle {
    String vehicleNumber;

    Vehicle(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public static void main(String[] args) {
        Vehicle v1 = new Vehicle("TS09AB1234");
        List<Vehicle> vehicles = List.of(v1); // Immutable collection

        // Mutating the element itself:
        v1.vehicleNumber = "TS09CD5678";

        System.out.println(
            vehicles.get(0).vehicleNumber
        ); // Output: TS09CD5678
        // Solution: Use immutable elements when the data must not change, or make defensive copies.
    }
}
