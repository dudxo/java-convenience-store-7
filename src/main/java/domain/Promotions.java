package domain;

import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<Promotion> getPromotions() {
        return List.copyOf(promotions);
    }

    public Promotion getPromotion(String promotionDetail) {
        return promotions.stream()
                .filter(promotion -> promotion.contains(promotionDetail))
                .findFirst()
                .orElse(null);
    }
}
