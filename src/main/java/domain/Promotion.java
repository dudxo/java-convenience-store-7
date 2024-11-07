package domain;

import java.time.LocalDate;

public class Promotion {

    private final PromotionType promotionType;
    private final int purchaseAmount;
    private final int giftAmount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(PromotionType promotionType, int purchaseAmount, int giftAmount, LocalDate startDate,
                     LocalDate endDate) {
        this.promotionType = promotionType;
        this.purchaseAmount = purchaseAmount;
        this.giftAmount = giftAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
