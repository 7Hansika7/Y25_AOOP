import java.util.Objects;

/**
 * Value Object: immutable, no identity, equality by value.
 * Amount is stored in paise (long) to avoid floating-point errors.
 */
public final class Money implements Comparable<Money> {

    private final long amountInPaise;
    private final String currency;

    public Money(long amountInPaise, String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }

    public static Money rupees(long rupees) {
        return new Money(rupees * 100, "INR");
    }

    public static Money zero() {
        return new Money(0, "INR");
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(Math.addExact(this.amountInPaise, other.amountInPaise), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(Math.subtractExact(this.amountInPaise, other.amountInPaise), currency);
    }

    public Money negate() {
        return new Money(-amountInPaise, currency);
    }

    public boolean isPositive() {
        return amountInPaise > 0;
    }

    public boolean isNegative() {
        return amountInPaise < 0;
    }

    public long getAmountInPaise() {
        return amountInPaise;
    }

    public String getCurrency() {
        return currency;
    }

    private void requireSameCurrency(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Money cannot be null");
        }
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: " + currency + " vs " + other.currency);
        }
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return Long.compare(amountInPaise, other.amountInPaise);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money m = (Money) o;
        return amountInPaise == m.amountInPaise && currency.equals(m.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountInPaise, currency);
    }

    @Override
    public String toString() {
        long abs = Math.abs(amountInPaise);
        String sign = amountInPaise < 0 ? "-" : "";
        return String.format("%s%s %d.%02d", sign, currency, abs / 100, abs % 100);
    }
}
