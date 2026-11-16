// Collections Overview in Java: List
// Maintains insertion order.
// Allows duplicate elements.
// Supports index-based access.

import java.util.ArrayList;
import java.util.List;

public class ListExample {
    public static void main(String[] args) {
        List<String> spots = new ArrayList<>();
        spots.add("P101");
        spots.add("P102");
        System.out.println("Spots: " + spots);
    }
}
