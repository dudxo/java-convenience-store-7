package view;

import domain.Storage;

public class OutputView {

    public void printWelcome() {
        ViewMessage.OUTPUT_WELCOME_MSG.print();
    }

    public void printStorage(Storage storage) {
        ViewMessage.OUTPUT_STOCK_ITEMS_MSG.print(storage.toString());
    }

    public void printAdditionalGiftQuantity(String name, int giftQuantity) {
        ViewMessage.OUTPUT_GET_ADDITIONAL_GIFT_MSG.print(name, giftQuantity);
    }

    public void printNotPromotionDiscount(String name, int lowQuantity) {
        ViewMessage.OUTPUT_NOT_PROMOTION_DISCOUNT_MSG.print(name, lowQuantity);
    }

    public void printMembership() {
        ViewMessage.OUTPUT_MEMBERSHIP_MSG.print();
    }


}
