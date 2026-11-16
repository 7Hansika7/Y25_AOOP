// Collections Overview in Java: Map
// Stores key-value pairs.
// Keys must be unique.
// Values can be duplicated.

import java.util.HashMap;
import java.util.Map;

public class MapExample {
    public static void main(String[] args) {
        Map<Integer, String> bookings = new HashMap<>();
        bookings.put(101, "Confirmed");
        bookings.put(102, "Pending");
        System.out.println("Bookings Map: " + bookings);
    }
}
