// 3. Money Is Not a Number
// Why should we avoid double for money?
// Reason: Computers store floating-point numbers using binary representation.
// Some decimal fractions, such as 0.1 and 0.2, cannot be represented exactly in binary floating-point format.
// Repeated calculations can accumulate rounding errors.

public class Main {
    public static void main(String[] args) {
        double fare = 0.1 + 0.2;
        System.out.println("0.1 + 0.2 with double: " + fare); // Output: 0.30000000000000004
    }
}
