import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity: has identity (accountNumber) and a lifecycle (ACTIVE -> FROZEN -> CLOSED).
 * Self-enforcing: every rule is checked inside the object; fields are private.
 */
public class BankAccount {

    // Ledger transaction types
    public static final String DEPOSIT      = "DEPOSIT";
    public static final String WITHDRAWAL   = "WITHDRAWAL";
    public static final String TRANSFER_IN  = "TRANSFER_IN";
    public static final String TRANSFER_OUT = "TRANSFER_OUT";
    public static final String REVERSAL     = "REVERSAL";

    private final String accountNumber;
    private Money balance;
    private final Money overdraftLimit;
    private AccountStatus status;
    private final List<LedgerEntry> ledger = new ArrayList<>();

    public BankAccount(String accountNumber, Money overdraftLimit) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (overdraftLimit == null || overdraftLimit.isNegative()) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.overdraftLimit = overdraftLimit;
        this.balance = Money.zero();
        this.status = AccountStatus.ACTIVE;
    }

    // ---------- Public operations ----------

    public void deposit(Money amount) {
        requirePositive(amount);
        requireNotClosed();
        credit(amount, DEPOSIT);
    }

    public void withdraw(Money amount) {
        requirePositive(amount);
        requireActive();
        requireWithinOverdraft(amount);
        debit(amount, WITHDRAWAL);
    }

    public void freeze() {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Closed account cannot be frozen");
        }
        status = AccountStatus.FROZEN;
    }

    public void unfreeze() {
        if (status != AccountStatus.FROZEN) {
            throw new IllegalStateException("Only a FROZEN account can be unfrozen");
        }
        status = AccountStatus.ACTIVE;
    }

    public void close() {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Account is already closed");
        }
        if (!balance.equals(Money.zero())) {
            throw new IllegalStateException("Account can be closed only when balance is zero");
        }
        status = AccountStatus.CLOSED;
    }

    // ---------- Package-private operations used by FundTransfer ----------

    void debitForTransfer(Money amount) {
        requirePositive(amount);
        requireActive();
        requireWithinOverdraft(amount);
        debit(amount, TRANSFER_OUT);
    }

    void creditForTransfer(Money amount) {
        requirePositive(amount);
        requireNotClosed();
        credit(amount, TRANSFER_IN);
    }

    /** Compensating action: history is never erased, a REVERSAL entry is added. */
    void reverseTransferDebit(Money amount) {
        credit(amount, REVERSAL);
    }

    void checkCanSend(Money amount) {
        requirePositive(amount);
        requireActive();
        requireWithinOverdraft(amount);
    }

    void checkCanReceive(Money amount) {
        requirePositive(amount);
        requireNotClosed();
    }

    // ---------- Consistency check: balance must equal sum of ledger ----------

    public boolean isLedgerConsistent() {
        Money total = Money.zero();
        for (LedgerEntry e : ledger) {
            String t = e.getTransactionType();
            boolean isCredit = t.equals(DEPOSIT) || t.equals(TRANSFER_IN) || t.equals(REVERSAL);
            total = isCredit ? total.add(e.getAmount()) : total.subtract(e.getAmount());
        }
        return total.equals(balance);
    }

    // ---------- Getters (no setters) ----------

    public String getAccountNumber()   { return accountNumber; }
    public Money getBalance()          { return balance; }
    public Money getOverdraftLimit()   { return overdraftLimit; }
    public AccountStatus getStatus()   { return status; }

    /** Read-only view: callers cannot modify the ledger. */
    public List<LedgerEntry> getLedger() {
        return Collections.unmodifiableList(ledger);
    }

    // ---------- Private helpers ----------

    private void credit(Money amount, String type) {
        balance = balance.add(amount);
        ledger.add(new LedgerEntry(type, amount, balance));
    }

    private void debit(Money amount, String type) {
        balance = balance.subtract(amount);
        ledger.add(new LedgerEntry(type, amount, balance));
    }

    private void requirePositive(Money amount) {
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
    }

    private void requireActive() {
        if (status == AccountStatus.FROZEN) {
            throw new IllegalStateException(
                    "Account " + accountNumber + " is FROZEN: withdrawal not allowed");
        }
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "Account " + accountNumber + " is CLOSED: withdrawal not allowed");
        }
    }

    private void requireNotClosed() {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "Account " + accountNumber + " is CLOSED: no transactions allowed");
        }
    }

    private void requireWithinOverdraft(Money amount) {
        Money newBalance = balance.subtract(amount);
        Money floor = overdraftLimit.negate();
        if (newBalance.compareTo(floor) < 0) {
            throw new IllegalStateException("Overdraft limit exceeded. Limit: " + overdraftLimit
                    + ", available to withdraw: " + balance.add(overdraftLimit));
        }
    }

    @Override
    public String toString() {
        return "Account[" + accountNumber + ", balance=" + balance + ", status=" + status + "]";
    }
}
