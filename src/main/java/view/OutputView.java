package view;

import domain.Order;
import domain.OrderItem;
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

    public void printActualPrice(Order order) {
        ViewMessage.OUTPUT_ACTUAL_PRICE.print(order.getActualTotalPrice());
    }

    public void printTotalMembershipDiscount(Order order) {
        ViewMessage.OUTPUT_MEMBERSHIP_DISCOUNT.print(-order.getMembershipDisCount());
    }

    public void printTotalPromotionDiscount(Order order) {
        ViewMessage.OUTPUT_EVENT_DISCOUNT.print(-order.getTotalOrderPromotionDiscount());
    }

    public void printTotalPurchasePrice(Order order) {
        ViewMessage.OUTPUT_TOTAL_PURCHASE_PRICE_MSG.print(order.getTotalOrderQuantity(), order.getTotalOrderPrice());
    }

    public void printGiftItemLine(int maxItemNameLength, OrderItem orderItem) {
        ViewMessage.OUTPUT_GIFT_LINE.printGiftLineWithDynamicFormat(maxItemNameLength, orderItem.getName(),
                orderItem.getPromotionQuantity());
    }

    public void printGiftHeader() {
        ViewMessage.OUTPUT_GIFT_HEADER.print();
    }

    public void printPurchaseItemLine(int maxItemNameLength, OrderItem orderItem) {
        ViewMessage.OUTPUT_ITEM_LINE.printItemLineWithDynamicFormat(maxItemNameLength, orderItem.getName(),
                orderItem.getPurchaseQuantity(),
                orderItem.getTotalPrice());
    }

    public void printReceiptHeader() {
        ViewMessage.OUTPUT_RECEIPT_HEADER.print();
    }

    public void printReceiptTag(int maxItemNameLength, String name, String quantity, String price) {
        ViewMessage.OUTPUT_RECEIPT_TAG.printItemLineWithDynamicFormat(maxItemNameLength, name, quantity, price);
    }

    public void printResultLine() {
        ViewMessage.OUTPUT_RESULT_LINE.print();
    }


}
