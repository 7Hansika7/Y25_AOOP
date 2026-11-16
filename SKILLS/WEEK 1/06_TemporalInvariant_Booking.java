// 3. Temporal invariants: Valid operation sequences over time
// Lifecycle: CREATED -> CONFIRMED -> ACTIVE -> COMPLETED

class Booking {
    private int bookingId;
    private String status;
    private int startTime;
    private int endTime;

    Booking(int bookingId, int startTime, int endTime) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException(
                "Invalid booking ID"
            );
        }
        if (endTime <= startTime) {
            throw new IllegalArgumentException(
                "End time must be after start time"
            );
        }
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "CREATED";
    }

    void confirmBooking() {
        if (!status.equals("CREATED")) {
            throw new IllegalStateException(
                "Only created bookings can be confirmed"
            );
        }
        status = "CONFIRMED";
    }

    void activateBooking() {
        if (!status.equals("CONFIRMED")) {
            throw new IllegalStateException(
                "Only confirmed bookings can be activated"
            );
        }
        status = "ACTIVE";
    }

    void completeBooking() {
        if (!status.equals("ACTIVE")) {
            throw new IllegalStateException(
                "Only active bookings can be completed"
            );
        }
        status = "COMPLETED";
    }

    void displayStatus() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Status: " + status);
    }

    public static void main(String[] args) {
        Booking b1 = new Booking(501, 10, 12);
        b1.confirmBooking();
        b1.activateBooking();
        b1.completeBooking();
        b1.displayStatus();
    }
}
