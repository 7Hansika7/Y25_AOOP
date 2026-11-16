// Modelling I: 1. Defensive Copying - With Defensive Copying
// The constructor stores a copy of the original list.
// The getter returns an unmodifiable copy.

import java.util.*;

class ParkingArea {
    private final List<String> spots;

    ParkingArea(List<String> spots) {
        this.spots = new ArrayList<>(spots);
    }

    public List<String> getSpots() {
        return List.copyOf(spots);
    }
}

public class Main {
    public static void main(String[] args) {
        List<String> spotList = new ArrayList<>(List.of("Spot-A", "Spot-B"));
        ParkingArea area = new ParkingArea(spotList);

        System.out.println("Spots: " + area.getSpots());
        // Attempting external modification will throw UnsupportedOperationException:
        // area.getSpots().add("Spot-C");
    }
}
