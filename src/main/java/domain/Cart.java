package domain;

import java.util.List;

public class Cart {
    private final List<CartItem> cartItems;

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return List.copyOf(cartItems);
    }
}
