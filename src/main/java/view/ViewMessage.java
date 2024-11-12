package view;

public enum ViewMessage {
    INPUT_PURCHASE_ITEMS_MSG("%n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])%n"),
    INPUT_ADDITION_PURCHASE_MSG("%n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)%n"),
    OUTPUT_WELCOME_MSG("%n안녕하세요. W편의점입니다.%n현재 보유하고 있는 상품입니다.%n%n"),
    OUTPUT_STOCK_ITEMS_MSG("%s"),
    OUTPUT_GET_ADDITIONAL_GIFT_MSG("%n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n"),
    OUTPUT_NOT_PROMOTION_DISCOUNT_MSG("%n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n"),
    OUTPUT_MEMBERSHIP_MSG("%n멤버십 할인을 받으시겠습니까? (Y/N)%n"),
    OUTPUT_RECEIPT_HEADER("%n==============W 편의점================%n"),
    OUTPUT_RECEIPT_TAG("%%-%ds\t%%10s\t%%10s%n"),

    OUTPUT_ITEM_LINE("%%-%ds\t%%10s\t%%,10d%n"),
    OUTPUT_GIFT_HEADER("=============증    정================%n"),
    OUTPUT_GIFT_LINE("%%-%ds\t%10d%n"),
    OUTPUT_RESULT_LINE("====================================%n"),
    OUTPUT_TOTAL_PURCHASE_PRICE_MSG("총구매액%10d\t%,10d%n"),
    OUTPUT_EVENT_DISCOUNT("행사할인\t\t\t%10s%,d%n"),
    OUTPUT_MEMBERSHIP_DISCOUNT("멤버십할인\t\t\t%10s%,d%n"),
    OUTPUT_ACTUAL_PRICE("내실돈\t\t\t\t%,10d%n");


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

    public void printReceiptTagWithDynamicFormat(int maxItemNameLength, Object... args) {
        String format = String.format("%%-%ds\t%%10s\t%%10s%n", maxItemNameLength);
        System.out.printf(format, args);
    }

    public void printItemLineWithDynamicFormat(int maxItemNameLength, Object... args) {
        String format = String.format("%%-%ds\t%%10s\t%%,10d%n", maxItemNameLength);
        System.out.printf(format, args);
    }

    public void printGiftLineWithDynamicFormat(int maxItemNameLength, Object... args) {
        String format = String.format("%%-%ds\t%%10d%n", maxItemNameLength);  // 포맷을 동적으로 생성
        System.out.printf(format, args);  // 생성된 포맷을 사용하여 출력
    }

}
