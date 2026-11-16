// Modelling I: 1. Defensive Copying - Without Defensive Copying
// Problem: If another class gets direct access to that list, it can add, remove or modify elements.

import java.util.*;

class ParkingArea {
    List<String> spots;

    ParkingArea(List<String> spots) {
        this.spots = spots;
    }

    public List<String> getSpots() {
        return spots;
    }
}

public class Main {
    public static void main(String[] args) {
        List<String> spotList = new ArrayList<>(List.of("Spot-A", "Spot-B"));
        ParkingArea area = new ParkingArea(spotList);

        System.out.println("Initial spots: " + area.getSpots());
        // External class directly modifies internal collection:
        area.getSpots().clear();
        System.out.println("After external modification: " + area.getSpots());
    }
}
