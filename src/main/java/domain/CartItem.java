package domain;

public class CartItem {

    private final String name;
    private int quantity;

    public CartItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void cancelItemQuantity(int lowQuantity) {
        this.quantity = this.quantity - lowQuantity;
    }

    public int calculateAvailableQuantity(int lowQuantity) {
        return this.quantity - lowQuantity;
    }

    public void addGiftQuantity(int giftQuantity) {
        this.quantity = this.quantity + giftQuantity;
    }

    public int calculateLowQuantity(int availableQuantity) {
        return this.getQuantity() - availableQuantity;
    }
}
