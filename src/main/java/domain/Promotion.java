package domain;

import java.time.LocalDateTime;

public class Promotion {

    private final PromotionType promotionType;
    private final int purchaseAmount;
    private final int giftAmount;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String promotion, int purchaseAmount, int giftAmount, LocalDateTime startDate,
                     LocalDateTime endDate) {
        this.promotionType = convertToPromotionType(promotion);
        this.purchaseAmount = purchaseAmount;
        this.giftAmount = giftAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private PromotionType convertToPromotionType(String promotion) {
        return PromotionType.of(promotion);
    }

    public boolean contains(String promotionDetail) {
        if (this.promotionType.getPromotionDetail().equals(promotionDetail)) {
            return true;
        }

        return false;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public int getGiftAmount() {
        return giftAmount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int calculateActualPurchaseQuantity(int requestedQuantity) {
        int totalSets = requestedQuantity / (purchaseAmount + giftAmount); // 10 /3 = 3
        int remainder = requestedQuantity % (purchaseAmount + giftAmount);  // 10 % 3 = 1

        return (totalSets * purchaseAmount) + remainder;  // 3 * 2 + 1
    }

    public int calculateRemainderQuantity(int requestedQuantity) {
        int remainder = requestedQuantity % (purchaseAmount + giftAmount);

        return remainder;  // 1
    }

    public int calculateActualPurchaseQuantity2(int requestedQuantity) {
        int totalSets = requestedQuantity / (purchaseAmount + giftAmount);

        return totalSets * purchaseAmount;  // 6
    }

    public int calculateGiftQuantity(int requestedQuantity) {
        int totalSets = requestedQuantity / (purchaseAmount + giftAmount);

        return totalSets * giftAmount;
    }

    public int getTotalPromotionQuantity() {
        return this.purchaseAmount + this.giftAmount;
    }

    public boolean isCurrentPromotion(LocalDateTime today) {
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public boolean isPromotionCondition(int requestedQuantity) {
        if (isBuyPromotionQuantity(requestedQuantity) && isTotalQuantity(requestedQuantity)) {
            return true;
        }

        return false;
    }

    private boolean isBuyPromotionQuantity(int requestedQuantity) {
        return this.purchaseAmount == requestedQuantity;
    }

    private boolean isTotalQuantity(int requestedQuantity) {
        return requestedQuantity < getTotalPromotionQuantity();
    }
}
