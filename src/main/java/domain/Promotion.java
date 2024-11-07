package domain;

import java.time.LocalDate;

public class Promotion {

    private final PromotionType promotionType;
    private final int purchaseAmount;
    private final int giftAmount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String promotion, int purchaseAmount, int giftAmount, LocalDate startDate, LocalDate endDate) {
        this.promotionType = convertToPromotionType(promotion);
        this.purchaseAmount = purchaseAmount;
        this.giftAmount = giftAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private PromotionType convertToPromotionType(String promotion) {
        return PromotionType.of(promotion);
    }
}
