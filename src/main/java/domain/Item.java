package domain;

public class RegularItem {

    private final String name;
    private final int price;
    private int quantity;

    public RegularItem(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return this.name + " "
                + formatPrice()
                + formatQuantity();
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

    public boolean containsName(String name) {
        if (this.name.equals(name)) {
            return true;
        }
        return false;
    }

    public boolean isPromotionItem() {
        return this.promotion.isPromotion();
    }

    public int getQuantity() {
        return quantity;
    }
}
