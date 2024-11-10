package domain;

import java.util.List;

public class Order {

    private final List<OrderItem> orderItems;
    private final int membershipDisCount;

    public Order(List<OrderItem> orderItems, int membershipDisCount) {
        this.orderItems = orderItems;
        this.membershipDisCount = membershipDisCount;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getMembershipDisCount() {
        return membershipDisCount;
    }

    public int getTotalOrderQuantity() {
        int totalOrderQuantity = 0;
        for (OrderItem orderItem : orderItems) {
            totalOrderQuantity += orderItem.getPurchaseQuantity();
        }

        return totalOrderQuantity;
    }

    public int getTotalOrderPrice() {
        int totalOrderPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalOrderPrice += orderItem.getTotalPrice();
        }

        return totalOrderPrice;
    }

    public int getTotalOrderPromotionDiscount() {
        int totalOrderPromotionDiscount = 0;
        for (OrderItem orderItem : orderItems) {
            totalOrderPromotionDiscount += orderItem.getTotalPromotionGiftPrice();
        }

        return totalOrderPromotionDiscount;
    }

    public int getActualTotalPrice() {
        return getTotalOrderPrice() - getTotalOrderPromotionDiscount() - this.membershipDisCount;
    }

    public int getMaxItemNameLength() {
        return orderItems.stream()
                .mapToInt(item -> item.getName().length())
                .max()
                .orElse(1);
    }
}
