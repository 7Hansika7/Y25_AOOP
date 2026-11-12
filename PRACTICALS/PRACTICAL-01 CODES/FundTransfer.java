/**
 * Atomic money transfer: either BOTH accounts change or NEITHER does.
 * Step 1: validate both sides first (fail fast, nothing changed).
 * Step 2: debit source, credit destination.
 * Step 3: if credit fails, compensate by reversing the debit (rollback).
 */
public class FundTransfer {

    private final String transferId;
    private final BankAccount source;
    private final BankAccount destination;
    private final Money amount;
    private boolean simulateCrashAfterDebit = false; // for demonstration only

    public FundTransfer(String transferId, BankAccount source, BankAccount destination, Money amount) {
        if (transferId == null || transferId.isBlank()) {
            throw new IllegalArgumentException("Transfer ID is required");
        }
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination accounts are required");
        }
        if (source.getAccountNumber().equals(destination.getAccountNumber())) {
            throw new IllegalArgumentException("Source and destination must be different accounts");
        }
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        this.transferId = transferId;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public void simulateCrashAfterDebit() {
        this.simulateCrashAfterDebit = true;
    }

    public synchronized void transfer() {
        // Step 1: validate both accounts BEFORE changing anything
        source.checkCanSend(amount);
        destination.checkCanReceive(amount);

        // Step 2: debit source
        source.debitForTransfer(amount);
        try {
            if (simulateCrashAfterDebit) {
                throw new RuntimeException("System crash after debit!");
            }
            // Step 3: credit destination
            destination.creditForTransfer(amount);
        } catch (RuntimeException ex) {
            // Rollback: put the money back using a REVERSAL ledger entry
            source.reverseTransferDebit(amount);
            throw new IllegalStateException(
                    "Transfer " + transferId + " failed and was rolled back: " + ex.getMessage());
        }
    }

    public String getTransferId() { return transferId; }
    public Money getAmount()      { return amount; }
}
