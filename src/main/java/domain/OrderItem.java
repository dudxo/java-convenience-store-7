package domain;

public class OrderItem {

    private String name;
    private int purchaseQuantity;
    private int promotionQuantity;
    private int price;

    public OrderItem(String item, int purchaseQuantity, int promotionQuantity, int price) {
        this.name = item;
        this.purchaseQuantity = purchaseQuantity;
        this.promotionQuantity = promotionQuantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPromotionQuantity() {
        if (this.promotionQuantity > 0) {
            return true;
        }

        return false;
    }


    public int calculateTotalPromotionQuantity(Promotion promotion) {
        return 0;
    }

    // 총 구매수량에서 프로모션 포함 수량 계산
    public int getGeneralQuantity(Promotion promotion) {
        int giftAmount = promotion.getGiftAmount();

        int totalSet = this.promotionQuantity / giftAmount;
        int totalPromotionQuantity = promotion.getTotalPromotionQuantity() * totalSet;

        return this.purchaseQuantity - totalPromotionQuantity;
    }

    public int getTotalPrice() {
        return this.purchaseQuantity * this.price;
    }

    public int getTotalPromotionPrice() {
        return this.promotionQuantity * this.price;
    }

}
