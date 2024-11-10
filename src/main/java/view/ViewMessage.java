package view;

public enum ViewMessage {
    INPUT_PURCHASE_ITEMS_MSG("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])%n"),
    INPUT_GET_FREE_ITEM_MSG("현재 %d은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n"),
    INPUT_NOT_PROMOTION_DISCOUNT_MSG("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n"),
    INPUT_MEMBERSHIP_DISCOUNT_MSG("멤버십 할인을 받으시겠습니까? (Y/N)%n"),
    INPUT_ADDITION_PURCHASE_MSG("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)%n"),
    OUTPUT_WELCOME_MSG("안녕하세요. W편의점입니다.%n현재 보유하고 있는 상품입니다.%n%n"),
    OUTPUT_STOCK_ITEMS_MSG("%s"),
    OUTPUT_GET_ADDITIONAL_GIFT_MSG("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n"),
    OUTPUT_NOT_PROMOTION_DISCOUNT_MSG("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n"),
    OUTPUT_MEMBERSHIP_MSG("멤버십 할인을 받으시겠습니까? (Y/N)%n"),
    ;


    private final String message;

    ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void print(Object... args) {
        System.out.printf(message, args);
    }

}
