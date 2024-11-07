package domain;

public class Product {

    private final String name;
    private final int price;
    private final PromotionType promotion;
    private int quantity;

    public Product(String name, int price, String promotion, int quantity) {
        this.name = name;
        this.price = price;
        this.promotion = of(promotion);
        this.quantity = quantity;
    }

    private static PromotionType of(String promotion) {
        return PromotionType.of(promotion);
    }
}
