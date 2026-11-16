// Practical Exercise - 1: Waypoint Demo
// Comprehensive urban mobility and parking reservation system demonstrating AOOP principles:
// - Typed Identifiers (Records)
// - Immutable Value Objects (Money)
// - Encapsulation & Invariant Protection
// - Tell, Don't Ask
// - Defensive Copying & Unmodifiable Views
// - State Lifecycle Management

import java.util.*;

// ==========================================
// 1. TYPED IDENTIFIERS
// ==========================================
record CustomerId(int value) {
    public CustomerId {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid Customer ID");
        }
    }
}

record BookingId(int value) {
    public BookingId {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid Booking ID");
        }
    }
}

record ParkingSpotId(int value) {
    public ParkingSpotId {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid Parking Spot ID");
        }
    }
}

record PaymentId(int value) {
    public PaymentId {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid Payment ID");
        }
    }
}

// ==========================================
// 2. MONEY - IMMUTABLE VALUE OBJECT
// ==========================================
record Money(long paise, String currency) {
    public Money {
        if (paise < 0) {
            throw new IllegalArgumentException(
                "Money cannot be negative"
            );
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException(
                "Currency cannot be empty"
            );
        }
        currency = currency.toUpperCase(Locale.ROOT);
    }

    public Money plus(Money other) {
        checkCurrency(other);
        return new Money(
            Math.addExact(this.paise, other.paise),
            currency
        );
    }

    public Money minus(Money other) {
        checkCurrency(other);
        if (this.paise < other.paise) {
            throw new IllegalArgumentException(
                "Insufficient amount"
            );
        }
        return new Money(
            this.paise - other.paise,
            currency
        );
    }

    public Money times(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                "Quantity cannot be negative"
            );
        }
        return new Money(
            Math.multiplyExact(this.paise, quantity),
            currency
        );
    }

    public List<Money> allocate(int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException(
                "Parts must be greater than zero"
            );
        }
        List<Money> result = new ArrayList<>();
        long baseAmount = paise / parts;
        long remainder = paise % parts;
        for (int i = 0; i < parts; i++) {
            long amount = baseAmount;
            if (i < remainder) {
                amount++;
            }
            result.add(new Money(amount, currency));
        }
        return List.copyOf(result);
    }

    private void checkCurrency(Money other) {
        if (other == null ||
            !this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Currency mismatch"
            );
        }
    }

    @Override
    public String toString() {
        return String.format(
            Locale.ROOT,
            "%s %d.%02d",
            currency,
            paise / 100,
            paise % 100
        );
    }
}

// ==========================================
// 3. CUSTOMER
// ==========================================
class Customer {
    private final CustomerId customerId;
    private final String name;

    public Customer(CustomerId customerId, String name) {
        if (customerId == null || name == null ||
            name.isBlank()) {
            throw new IllegalArgumentException(
                "Invalid customer details"
            );
        }
        this.customerId = customerId;
        this.name = name;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

// ==========================================
// 4. PARKING SPOT
// ==========================================
enum SpotStatus {
    AVAILABLE,
    OCCUPIED
}

class ParkingSpot {
    private final ParkingSpotId spotId;
    private final String location;
    private SpotStatus status;
    private String vehicleNumber;

    public ParkingSpot(
        ParkingSpotId spotId,
        String location
    ) {
        if (spotId == null || location == null ||
            location.isBlank()) {
            throw new IllegalArgumentException(
                "Invalid parking spot details"
            );
        }
        this.spotId = spotId;
        this.location = location;
        this.status = SpotStatus.AVAILABLE;
        this.vehicleNumber = null;
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    // Tell, Don't Ask:
    // ParkingSpot manages its own state.
    public void parkVehicle(String vehicleNumber) {
        if (!isAvailable()) {
            throw new IllegalStateException(
                "Parking spot is already occupied"
            );
        }
        if (vehicleNumber == null ||
            vehicleNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Invalid vehicle number"
            );
        }
        this.vehicleNumber = vehicleNumber;
        this.status = SpotStatus.OCCUPIED;
    }

    public void releaseSpot() {
        if (isAvailable()) {
            throw new IllegalStateException(
                "Parking spot is already available"
            );
        }
        this.vehicleNumber = null;
        this.status = SpotStatus.AVAILABLE;
    }

    public ParkingSpotView getSnapshot() {
        return new ParkingSpotView(
            spotId,
            location,
            status,
            vehicleNumber
        );
    }
}

// ==========================================
// 5. IMMUTABLE PARKING SPOT VIEW
// ==========================================
record ParkingSpotView(
    ParkingSpotId spotId,
    String location,
    SpotStatus status,
    String vehicleNumber
) {}

// ==========================================
// 6. PAYMENT
// ==========================================
record Payment(
    PaymentId paymentId,
    BookingId bookingId,
    Money amount,
    boolean successful
) {
    public Payment {
        if (paymentId == null ||
            bookingId == null ||
            amount == null) {
            throw new IllegalArgumentException(
                "Invalid payment details"
            );
        }
    }
}

// ==========================================
// 7. BOOKING STATE
// ==========================================
enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}

// ==========================================
// 8. BOOKING
// ==========================================
class Booking {
    private final BookingId bookingId;
    private final CustomerId customerId;
    private final ParkingSpotId spotId;
    private final String vehicleNumber;
    private final Money fare;
    private BookingStatus status;
    private Payment payment;

    public Booking(
        BookingId bookingId,
        CustomerId customerId,
        ParkingSpotId spotId,
        String vehicleNumber,
        Money fare
    ) {
        if (bookingId == null ||
            customerId == null ||
            spotId == null ||
            vehicleNumber == null ||
            vehicleNumber.isBlank() ||
            fare == null) {
            throw new IllegalArgumentException(
                "Invalid booking details"
            );
        }
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.spotId = spotId;
        this.vehicleNumber = vehicleNumber;
        this.fare = fare;
        // A new booking always starts as PENDING.
        this.status = BookingStatus.PENDING;
        this.payment = null;
    }

    // Payment must be successful before confirmation.
    public void attachPayment(Payment payment) {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException(
                "Payment can only be attached to a pending booking"
            );
        }
        if (payment == null || !payment.successful()) {
            throw new IllegalArgumentException(
                "Payment was unsuccessful"
            );
        }
        if (!payment.bookingId().equals(bookingId)) {
            throw new IllegalArgumentException(
                "Payment belongs to another booking"
            );
        }
        if (!payment.amount().equals(fare)) {
            throw new IllegalArgumentException(
                "Payment amount does not match booking fare"
            );
        }
        this.payment = payment;
        // State transition: PENDING -> CONFIRMED
        this.status = BookingStatus.CONFIRMED;
    }

    // Only confirmed bookings can be cancelled.
    public void cancel() {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException(
                "Only confirmed bookings can be cancelled"
            );
        }
        // State transition: CONFIRMED -> CANCELLED
        this.status = BookingStatus.CANCELLED;
    }

    public BookingId getBookingId() {
        return bookingId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public ParkingSpotId getSpotId() {
        return spotId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Money getFare() {
        return fare;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Payment getPayment() {
        return payment;
    }
}

// ==========================================
// 9. WAYPOINT APPLICATION
// ==========================================
class Waypoint {
    private final Map<CustomerId, Customer> customers =
        new HashMap<>();
    private final Map<ParkingSpotId, ParkingSpot> spots =
        new HashMap<>();
    private final Map<BookingId, Booking> bookings =
        new HashMap<>();
    private final Set<String> registeredVehicles =
        new HashSet<>();
    private int bookingCounter = 1;
    private int paymentCounter = 1;

    // --------------------------------------
    // Register customer
    // --------------------------------------
    public void registerCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException(
                "Customer cannot be null"
            );
        }
        if (customers.containsKey(customer.getCustomerId())) {
            throw new IllegalArgumentException(
                "Customer already registered"
            );
        }
        customers.put(customer.getCustomerId(), customer);
    }

    // --------------------------------------
    // Add parking spot
    // --------------------------------------
    public void addParkingSpot(ParkingSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException(
                "Parking spot cannot be null"
            );
        }
        ParkingSpotId id = spot.getSnapshot().spotId();
        if (spots.containsKey(id)) {
            throw new IllegalArgumentException(
                "Parking spot already exists"
            );
        }
        spots.put(id, spot);
    }

    // --------------------------------------
    // Register vehicle
    // --------------------------------------
    public void registerVehicle(String vehicleNumber) {
        if (vehicleNumber == null ||
            vehicleNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Invalid vehicle number"
            );
        }
        String normalized =
            vehicleNumber.trim().toUpperCase(Locale.ROOT);
        if (!registeredVehicles.add(normalized)) {
            throw new IllegalArgumentException(
                "Vehicle already registered"
            );
        }
    }

    // --------------------------------------
    // Display parking spots
    // --------------------------------------
    public List<ParkingSpotView> getParkingSpots() {
        List<ParkingSpotView> result = new ArrayList<>();
        for (ParkingSpot spot : spots.values()) {
            result.add(spot.getSnapshot());
        }
        // Return an unmodifiable copy, not internal objects.
        return List.copyOf(result);
    }

    // --------------------------------------
    // Reserve parking spot
    // --------------------------------------
    public Booking reserve(
        CustomerId customerId,
        ParkingSpotId spotId,
        String vehicleNumber,
        int hours,
        Money hourlyRate
    ) {
        if (!customers.containsKey(customerId)) {
            throw new IllegalArgumentException(
                "Customer not found"
            );
        }
        if (hours <= 0) {
            throw new IllegalArgumentException(
                "Booking hours must be greater than zero"
            );
        }
        if (hourlyRate == null) {
            throw new IllegalArgumentException(
                "Hourly rate cannot be null"
            );
        }
        String normalizedVehicle =
            vehicleNumber == null
                ? ""
                : vehicleNumber.trim().toUpperCase(Locale.ROOT);
        if (!registeredVehicles.contains(normalizedVehicle)) {
            throw new IllegalArgumentException(
                "Vehicle is not registered"
            );
        }
        ParkingSpot spot = spots.get(spotId);
        if (spot == null) {
            throw new IllegalArgumentException(
                "Parking spot not found"
            );
        }
        if (!spot.isAvailable()) {
            throw new IllegalStateException(
                "Parking spot is not available"
            );
        }
        Money totalFare = hourlyRate.times(hours);
        BookingId bookingId =
            new BookingId(bookingCounter++);
        PaymentId paymentId =
            new PaymentId(paymentCounter++);
        boolean vehicleParked = false;
        try {
            // Step 1: Reserve the parking spot.
            spot.parkVehicle(normalizedVehicle);
            vehicleParked = true;
            // Step 2: Create a PENDING booking.
            Booking booking = new Booking(
                bookingId,
                customerId,
                spotId,
                normalizedVehicle,
                totalFare
            );
            // Step 3: Simulate successful payment.
            Payment payment = new Payment(
                paymentId,
                bookingId,
                totalFare,
                true
            );
            // Step 4: Attach payment.
            // Booking changes from PENDING to CONFIRMED.
            booking.attachPayment(payment);
            // Step 5: Store the confirmed booking.
            bookings.put(bookingId, booking);
            return booking;
        } catch (RuntimeException exception) {
            // Roll back parking spot if booking fails.
            if (vehicleParked) {
                spot.releaseSpot();
            }
            throw exception;
        }
    }

    // --------------------------------------
    // Cancel booking
    // --------------------------------------
    public void cancelBooking(BookingId bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException(
                "Booking not found"
            );
        }
        // First validate and change booking state.
        booking.cancel();
        // Release the parking spot.
        ParkingSpot spot = spots.get(booking.getSpotId());
        spot.releaseSpot();
    }

    // --------------------------------------
    // Display all bookings
    // --------------------------------------
    public void displayBookings() {
        System.out.println("\n----- BOOKING DETAILS -----");
        for (Booking booking : bookings.values()) {
            System.out.println(
                "Booking ID: " + booking.getBookingId().value()
            );
            System.out.println(
                "Customer ID: " + booking.getCustomerId().value()
            );
            System.out.println(
                "Parking Spot ID: " + booking.getSpotId().value()
            );
            System.out.println(
                "Vehicle Number: " + booking.getVehicleNumber()
            );
            System.out.println(
                "Fare: " + booking.getFare()
            );
            System.out.println(
                "Booking Status: " + booking.getStatus()
            );
            System.out.println("---------------------------");
        }
    }
}

// ==========================================
// 10. MAIN CLASS
// ==========================================
public class WaypointDemo {
    public static void main(String[] args) {
        Waypoint waypoint = new Waypoint();
        // ----------------------------------
        // Step 1: Register customer
        // ----------------------------------
        Customer customer = new Customer(
            new CustomerId(101),
            "Ramya"
        );
        waypoint.registerCustomer(customer);
        System.out.println("Customer registered successfully.");
        // ----------------------------------
        // Step 2: Add parking spots
        // ----------------------------------
        ParkingSpot spot1 = new ParkingSpot(
            new ParkingSpotId(201),
            "Bachupally"
        );
        ParkingSpot spot2 = new ParkingSpot(
            new ParkingSpotId(202),
            "Nizampet"
        );
        waypoint.addParkingSpot(spot1);
        waypoint.addParkingSpot(spot2);
        System.out.println("Parking spots added successfully.");
        // ----------------------------------
        // Step 3: Register vehicle
        // ----------------------------------
        waypoint.registerVehicle("TS09AB1234");
        System.out.println("Vehicle registered successfully.");
        // ----------------------------------
        // Step 4: Display parking availability
        // ----------------------------------
        System.out.println("\n----- PARKING AVAILABILITY -----");
        for (ParkingSpotView view :
             waypoint.getParkingSpots()) {
            System.out.println(
                "Spot ID: " + view.spotId().value()
            );
            System.out.println(
                "Location: " + view.location()
            );
            System.out.println(
                "Status: " + view.status()
            );
            System.out.println("---------------------------");
        }
        // ----------------------------------
        // Step 5: Book a parking spot
        // ----------------------------------
        System.out.println("\n----- BOOKING PARKING -----");
        Booking booking = waypoint.reserve(
            new CustomerId(101),
            new ParkingSpotId(201),
            "TS09AB1234",
            2,
            new Money(5000, "INR")  // Rs. 50 per hour
        );
        System.out.println(
            "Booking ID: " + booking.getBookingId().value()
        );
        System.out.println(
            "Total Fare: " + booking.getFare()
        );
        System.out.println(
            "Payment Status: " +
            booking.getPayment().successful()
        );
        System.out.println(
            "Booking Status: " + booking.getStatus()
        );
        // ----------------------------------
        // Step 6: Try booking the same spot
        // ----------------------------------
        System.out.println(
            "\n----- TRYING DUPLICATE BOOKING -----"
        );
        try {
            waypoint.reserve(
                new CustomerId(101),
                new ParkingSpotId(201),
                "TS09AB1234",
                1,
                new Money(5000, "INR")
            );
        } catch (IllegalStateException exception) {
            System.out.println(
                "Booking failed: " + exception.getMessage()
            );
        }
        // ----------------------------------
        // Step 7: Cancel booking
        // ----------------------------------
        System.out.println("\n----- CANCELLING BOOKING -----");
        waypoint.cancelBooking(booking.getBookingId());
        System.out.println(
            "Booking Status After Cancellation: " +
            booking.getStatus()
        );
        // ----------------------------------
        // Step 8: Display parking availability
        // ----------------------------------
        System.out.println(
            "\n----- PARKING AVAILABILITY AFTER CANCELLATION -----"
        );
        for (ParkingSpotView view :
             waypoint.getParkingSpots()) {
            System.out.println(
                "Spot ID: " + view.spotId().value()
            );
            System.out.println(
                "Location: " + view.location()
            );
            System.out.println(
                "Status: " + view.status()
            );
            System.out.println("---------------------------");
        }
        // ----------------------------------
        // Step 9: Display all bookings
        // ----------------------------------
        waypoint.displayBookings();
    }
}
