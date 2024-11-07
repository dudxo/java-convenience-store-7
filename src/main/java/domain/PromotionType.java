package domain;

import java.util.Arrays;

public enum PromotionType {

    NO_PROMOTION(false, ""),
    CARBONATED_DRINK_BYE_TWO_GET_ONE(true, "탄산2+1"),
    RECOMMENDED_PRODUCT(true, "MD추천상품"),
    FLASH_DISCOUNT(true, "반짝할인");


    private final boolean isPromotion;
    private final String promotionDetail;

    PromotionType(boolean isPromotion, String promotionDetail) {
        this.isPromotion = isPromotion;
        this.promotionDetail = promotionDetail;
    }

    public static PromotionType of(String promotionDetail) {
        return Arrays.stream(values())
                .filter(promotionType -> promotionType.isEqual(promotionDetail))
                .findFirst()
                .orElse(NO_PROMOTION);
    }

    private boolean isEqual(String promotionDetail) {
        return promotionDetail.equals(this.promotionDetail);
    }

    @Override
    public String toString() {
        return this.promotionDetail;
    }
}
