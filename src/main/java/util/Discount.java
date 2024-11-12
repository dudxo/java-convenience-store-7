package util;

public enum Discount {

    DISCOUNT_RATE(0.3),
    MAX_MEMBERSHIP_DISCOUNT(8000.0);

    private final double number;

    Discount(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }
}