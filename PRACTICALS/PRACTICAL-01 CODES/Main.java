public class Main {

    static void line(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    static void expectError(String label, Runnable action) {
        try {
            action.run();
            System.out.println("[FAIL] " + label + " -> no error thrown");
        } catch (RuntimeException e) {
            String msg = (e.getMessage() != null) ? e.getMessage() : e.getClass().getSimpleName();
            System.out.println("[OK]   " + label + " -> rejected: " + msg);
        }
    }

    public static void main(String[] args) {

        BankAccount a = new BankAccount("ACC1001", Money.rupees(5000));  // overdraft Rs 5000
        BankAccount b = new BankAccount("ACC1002", Money.rupees(2000));

        line("Test 1: Valid deposit");
        a.deposit(Money.rupees(20000));
        b.deposit(Money.rupees(3000));
        System.out.println(a);
        System.out.println(b);

        line("Test 2: Valid withdrawal");
        a.withdraw(Money.rupees(2000));
        System.out.println(a);

        line("Test 3: Invalid amounts (zero / negative / null)");
        expectError("Deposit 0",         () -> a.deposit(Money.rupees(0)));
        expectError("Deposit -500",      () -> a.deposit(Money.rupees(-500)));
        expectError("Withdraw -100",     () -> a.withdraw(Money.rupees(-100)));
        expectError("Withdraw null",     () -> a.withdraw(null));

        line("Test 4: Overdraft limit");
        BankAccount c = new BankAccount("ACC1003", Money.rupees(5000));
        c.deposit(Money.rupees(1000));
        c.withdraw(Money.rupees(5000));           // balance = -4000, within limit
        System.out.println("Within limit    : " + c);
        expectError("Withdraw 2000 (beyond overdraft)", () -> c.withdraw(Money.rupees(2000)));

        line("Test 5: Frozen account");
        b.freeze();
        System.out.println(b);
        expectError("Withdraw from frozen", () -> b.withdraw(Money.rupees(100)));
        b.unfreeze();
        b.withdraw(Money.rupees(100));
        System.out.println("After unfreeze  : " + b);

        line("Test 6: Closed account");
        BankAccount d = new BankAccount("ACC1004", Money.rupees(0));
        d.deposit(Money.rupees(500));
        expectError("Close with balance", d::close);
        d.withdraw(Money.rupees(500));
        d.close();
        System.out.println(d);
        expectError("Withdraw from closed", () -> d.withdraw(Money.rupees(10)));
        expectError("Deposit to closed",    () -> d.deposit(Money.rupees(10)));
        expectError("Unfreeze closed",      d::unfreeze);

        line("Test 7: Successful transfer of Rs 5000 (A -> B)");
        System.out.println("Before: " + a + " | " + b);
        new FundTransfer("TRF-001", a, b, Money.rupees(5000)).transfer();
        System.out.println("After : " + a + " | " + b);

        line("Test 8: Crash during transfer -> rollback");
        Money aBefore = a.getBalance();
        Money bBefore = b.getBalance();
        System.out.println("Before: " + a + " | " + b);
        FundTransfer bad = new FundTransfer("TRF-002", a, b, Money.rupees(5000));
        bad.simulateCrashAfterDebit();
        expectError("Transfer with crash", bad::transfer);
        System.out.println("After : " + a + " | " + b);
        System.out.println("A balance unchanged: " + a.getBalance().equals(aBefore));
        System.out.println("B balance unchanged: " + b.getBalance().equals(bBefore));

        line("Test 9: Invalid transfers");
        expectError("Transfer to same account",
                () -> new FundTransfer("TRF-003", a, a, Money.rupees(100)));
        expectError("Transfer to closed account",
                () -> new FundTransfer("TRF-004", a, d, Money.rupees(100)).transfer());
        expectError("Transfer beyond overdraft",
                () -> new FundTransfer("TRF-005", c, b, Money.rupees(9000)).transfer());

        line("Test 10: Ledger of Account A (immutable history)");
        for (LedgerEntry e : a.getLedger()) {
            System.out.println(e);
        }
        expectError("Modify ledger directly", () -> a.getLedger().clear());

        line("Test 11: Balance vs ledger consistency");
        System.out.println("A consistent: " + a.isLedgerConsistent());
        System.out.println("B consistent: " + b.isLedgerConsistent());
        System.out.println("C consistent: " + c.isLedgerConsistent());
        System.out.println("D consistent: " + d.isLedgerConsistent());
    }
}
