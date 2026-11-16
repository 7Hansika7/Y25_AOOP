// Collections Overview in Java: Set
// Does not allow duplicate elements.
// Useful for maintaining unique values (e.g., registered vehicle numbers).

import java.util.HashSet;
import java.util.Set;

public class SetExample {
    public static void main(String[] args) {
        Set<String> vehicles = new HashSet<>();
        vehicles.add("TS09AB1234");
        vehicles.add("TS09AB1234");
        System.out.println("Vehicles Set (no duplicates): " + vehicles);
    }
}
