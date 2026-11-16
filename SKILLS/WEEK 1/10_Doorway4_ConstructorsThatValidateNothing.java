// Doorway 4: Constructors that validate nothing vs Constructor Validation

// Problem: The object accepts an invalid payment amount.
class PaymentWeak {
    private double amount;

    PaymentWeak(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

// Solution: Validate data during object construction.
class PaymentValidated {
    private double amount;

    PaymentValidated(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Payment amount must be positive"
            );
        }
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

public class Main {
    public static void main(String[] args) {
        // Problem:
        PaymentWeak p1 = new PaymentWeak(-500);
        System.out.println("Weak constructor created invalid payment: " + p1.getAmount());

        // Solution:
        try {
            PaymentValidated p2 = new PaymentValidated(-500);
        } catch (IllegalArgumentException e) {
            System.out.println("Validated constructor prevented invalid payment: " + e.getMessage());
        }
    }
}
