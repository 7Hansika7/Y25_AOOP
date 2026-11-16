// Collections Overview in Java: Mutable vs Immutable Collections

import java.util.ArrayList;
import java.util.List;

public class MutableVsImmutableExample {
    public static void main(String[] args) {
        // Mutable collection: Elements can be added, removed or modified.
        List<String> mutableSpots = new ArrayList<>();
        mutableSpots.add("P101");
        mutableSpots.add("P102");
        mutableSpots.remove("P101");
        System.out.println("Mutable collection: " + mutableSpots);

        // Immutable or unmodifiable collection: You cannot add or remove elements.
        List<String> immutableSpots = List.of("P101", "P102");
        System.out.println("Immutable collection: " + immutableSpots);
        // immutableSpots.add("P103"); // Throws UnsupportedOperationException
    }
}
