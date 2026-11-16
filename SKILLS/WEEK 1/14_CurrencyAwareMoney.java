// 4. Currency-aware and immutable Money class
// Store money as long paise to avoid binary floating-point rounding errors.

class Money {
    private final long paise;
    private final String currency;

    Money(long paise, String currency) {
        if (paise < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative");
        }
        this.paise = paise;
        this.currency = currency;
    }

    public Money plus(Money other) {
        checkCurrency(other);
        return new Money(paise + other.paise, currency);
    }

    public Money minus(Money other) {
        checkCurrency(other);
        if (paise < other.paise) {
            throw new IllegalArgumentException(
                "Insufficient amount");
        }
        return new Money(paise - other.paise, currency);
    }

    private void checkCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Currency mismatch");
        }
    }

    public long getPaise() {
        return paise;
    }

    public String getCurrency() {
        return currency;
    }

    public static void main(String[] args) {
        Money m1 = new Money(10000, "INR"); // Rs. 100
        Money m2 = new Money(5000, "INR");  // Rs. 50
        Money result = m1.plus(m2);
        System.out.println("Result: " + result.getPaise() + " paise (" + result.getCurrency() + ")");
    }
}
