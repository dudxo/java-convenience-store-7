package domain;

public class OrderItem {

    private String name;
    private int purchaseQuantity;
    private int promotionGiftQuantity;
    private int price;

    public OrderItem(String item, int purchaseQuantity, int promotionQuantity, int price) {
        this.name = item;
        this.purchaseQuantity = purchaseQuantity;
        this.promotionGiftQuantity = promotionQuantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public int getPromotionQuantity() {
        return promotionGiftQuantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean getPromotionGiftQuantity() {
        if (this.promotionGiftQuantity > 0) {
            return true;
        }

        return false;
    }


    public int calculateTotalPromotionQuantity(Promotion promotion) {
        return 0;
    }

    // 총 구매수량에서 일반 재고 구매 수량을 계산한다.
    public int getGeneralQuantity(Promotion promotion) {
        int giftAmount = promotion.getGiftAmount();

        int totalSet = this.promotionGiftQuantity / giftAmount;
        int totalPromotionQuantity = promotion.getTotalPromotionQuantity() * totalSet;

        return this.purchaseQuantity - totalPromotionQuantity;
    }

    public int getTotalPrice() {
        return this.purchaseQuantity * this.price;
    }

    public int getTotalPromotionGiftPrice() {
        return this.promotionGiftQuantity * this.price;
    }

}
