// Modelling I: 2. Typed IDs - Problem
// Primitive types allow passing the wrong ID by mistake without compiler error.

public class Main {
    static void cancelBooking(int bookingId) {
        System.out.println("Booking cancelled for ID: " + bookingId);
    }

    public static void main(String[] args) {
        int customerId = 101;
        int bookingId = 201;
        int parkingSpotId = 301;

        // This compiles successfully even though we passed a customer ID instead of a booking ID!
        cancelBooking(customerId);
    }
}
