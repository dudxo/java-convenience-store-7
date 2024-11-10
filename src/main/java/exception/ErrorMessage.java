package exception;

public enum ErrorMessage {

    INVALID_PURCHASE_INPUT_MSG("올바르지 않은 형식으로 입력했습니다."),
    NOT_FOUND_ITEM_MSG("존재하지 않는 상품입니다."),
    EXCEED_STOCK_MSG("재고 수량을 초과하여 구매할 수 없습니다."),
    INVALID_ANSWER_MSG("잘못된 입력입니다.");


    private static final String PREFIX_ERROR_MSG = "[ERROR] ";
    private static final String SUFFIX_ERROR_MSG = " 다시 입력해 주세요.";

    private final String message;

    ErrorMessage(String message) {
        this.message = PREFIX_ERROR_MSG + message + SUFFIX_ERROR_MSG;
    }

    public String getMessage() {
        return message;
    }
}
