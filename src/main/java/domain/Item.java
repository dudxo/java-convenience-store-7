package domain;

public class Item {

    private final String name;
    private final int price;
    private final PromotionType promotion;
    private int quantity;

    public Item(String name, int price, String promotion, int quantity) {
        this.name = name;
        this.price = price;
        this.promotion = convertToPromotionType(promotion);
        this.quantity = quantity;
    }

    private PromotionType convertToPromotionType(String promotion) {
        return PromotionType.of(promotion);
    }

    @Override
    public String toString() {
        return this.name + " "
                + formatPrice()
                + formatQuantity()
                + this.promotion.toString();
    }

    private String formatPrice() {
        return String.format("%,d", this.price) + "원 ";
    }

    private String formatQuantity() {
        final String ZERO_QUANTITY = "재고 없음";
        if (this.quantity == 0) {
            return ZERO_QUANTITY;
        }
        final String ONE_OR_THAN_QUANTITY = this.quantity + "개 ";
        return ONE_OR_THAN_QUANTITY;
    }
}
