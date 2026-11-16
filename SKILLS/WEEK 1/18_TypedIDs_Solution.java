// Modelling I: 2. Typed IDs - Solution
// Using separate types (Java records) prevents accidentally passing the wrong ID.

record CustomerId(int value) {}
record BookingId(int value) {}
record ParkingSpotId(int value) {}

public class Main {
    static void cancelBooking(BookingId bookingId) {
        System.out.println("Booking cancelled for ID: " + bookingId.value());
    }

    public static void main(String[] args) {
        CustomerId customerId = new CustomerId(101);
        BookingId bookingId = new BookingId(201);

        cancelBooking(bookingId);
        // cancelBooking(customerId); // Compilation error: incompatible types
    }
}
