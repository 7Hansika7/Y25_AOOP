import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Entity record that is IMMUTABLE: once written it is never modified or deleted.
 * Corrections are made by adding a new (reversal) entry.
 */
public final class LedgerEntry {

    private static final AtomicLong COUNTER = new AtomicLong(1000);

    private final String transactionId;
    private final String transactionType;
    private final Money amount;
    private final Money balanceAfter;
    private final String timestamp;

    public LedgerEntry(String transactionType, Money amount, Money balanceAfter) {
        if (transactionType == null || transactionType.isBlank()) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Ledger amount must be positive");
        }
        if (balanceAfter == null) {
            throw new IllegalArgumentException("Balance after transaction is required");
        }
        this.transactionId = "TXN" + COUNTER.incrementAndGet();
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getTransactionId()   { return transactionId; }
    public String getTransactionType() { return transactionType; }
    public Money getAmount()           { return amount; }
    public Money getBalanceAfter()     { return balanceAfter; }
    public String getTimestamp()       { return timestamp; }

    @Override
    public String toString() {
        return String.format("%s | %-15s | %-14s | Balance: %s | %s",
                transactionId, transactionType, amount, balanceAfter, timestamp);
    }
}
