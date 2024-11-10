package controller;

import camp.nextstep.edu.missionutils.DateTimes;
import domain.Cart;
import domain.CartItem;
import domain.Item;
import domain.Order;
import domain.OrderItem;
import domain.Promotion;
import domain.Promotions;
import domain.Storage;
import file.FileLoader;
import java.util.ArrayList;
import java.util.List;
import util.DependencyFactory;
import valid.Task;
import valid.Validate;
import view.InputView;
import view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private Storage storage;
    private Promotions promotions;

    public StoreController(DependencyFactory factory) {
        this.inputView = factory.getInputView();
        this.outputView = factory.getOutputView();
    }

    public void run() {
        this.storage = initStorage();
        this.promotions = initPromotions();
        printStartMessage();

        Order order = playStore();
        printReceipt(order);
        outputView.printStorage(storage);
    }

    private Order playStore() {
        return Task.reTryTaskUntilSuccessful(() -> {
            List<Item> items = storage.getItems();
            this.storage = new Storage(items);
            Cart cart = getCart();
            Order order = initOrder(cart);
            return order;
        });

    }

    private void printStartMessage() {
        outputView.printWelcome();
        outputView.printStorage(storage);
    }

    private Storage initStorage() {
        List<Item> items = FileLoader.loadProductsFromFile();
        return new Storage(items);
    }

    private Promotions initPromotions() {
        List<Promotion> promotions = FileLoader.loadPromotionsFromFile();
        return new Promotions(promotions);
    }


    public Cart getCart() {
        List<CartItem> cartItems = Task.reTryTaskUntilSuccessful(() -> getCartItem());
        return new Cart(cartItems);
    }

    private List<CartItem> getCartItem() {
        List<CartItem> cartItems = new ArrayList<>();
        for (String inputItem : inputItem()) {
            cartItems.add(generateCartItem(inputItem));
        }
        return cartItems;
    }

    private CartItem generateCartItem(String inputItem) {
        String[] itemParts = inputItem.substring(1, inputItem.length() - 1).split("-");
        String itemName = itemParts[0];
        int itemQuantity = Integer.parseInt(itemParts[1]);
        Validate.existItemNameInStorage(storage, itemName);
        return new CartItem(itemName, itemQuantity);
    }

    public Order initOrder(Cart cart) {
        List<OrderItem> orderItems = new ArrayList<>();
        int membershipDiscount = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            Item promotionItem = storage.findPromotionItem(cartItem.getName());
            Item generalItem = storage.findGeneralItem(cartItem.getName());
            // 프로모션 상품이라면
            if (storage.isPromotion(cartItem.getName())) {
                String promotionDetail = promotionItem.getPromotionDetail();
                Promotion promotion = promotions.getPromotion(promotionDetail);
                //오늘 프로모션 날짜인지
                if (promotion.isCurrentPromotion(DateTimes.now())) {
                    promotion(cartItem, promotionItem, generalItem, promotions, orderItems);
                    continue;
                }
            }
            // 프로모션이 아닐때
            // TODO : 일반 상품 재고량 구매 수량과 비교
            Validate.validateEnoughStock(storage, cartItem);
            orderItems.add(new OrderItem(cartItem.getName(), cartItem.getQuantity(), 0, generalItem.getPrice()));
            generalItem.updateQuantity(cartItem.getQuantity());
        }

        outputView.printMembership();
        String answer = Task.reTryTaskUntilSuccessful(() -> inputAnswer());
        if (Validate.isYesAnswer(answer)) {
            int totalGeneralPriceSum = 0;
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getPromotionGiftQuantity()) {
                    Item promotionItem = storage.findPromotionItem(orderItem.getName());
                    String promotionDetail = promotionItem.getPromotionDetail();
                    Promotion promotion = promotions.getPromotion(promotionDetail);
                    int generalQuantity = orderItem.getGeneralQuantity(promotion);
                    totalGeneralPriceSum += generalQuantity * orderItem.getPrice();
                    continue;
                }
                totalGeneralPriceSum += orderItem.getTotalPrice();
            }
            membershipDiscount = (int) (totalGeneralPriceSum * 0.3);
            if (membershipDiscount > 8000) {
                membershipDiscount = 8000;
            }
        }

        if (Validate.isNoAnswer(answer)) {
            membershipDiscount = 0;
        }

        return new Order(orderItems, membershipDiscount);
    }

    private void promotion(CartItem cartItem, Item promotionItem, Item generalItem, Promotions promotions,
                           List<OrderItem> orderItems) {
        if (promotionItem != null) {
            String promotionDetail = promotionItem.getPromotionDetail();
            Promotion promotion = promotions.getPromotion(promotionDetail);
            int availableQuantity = promotionItem.availableQuantity(promotion.getTotalPromotionQuantity());

            // 구매 수량보다 프로모션 재고량이 부족한 경우
            if (availableQuantity < cartItem.getQuantity()) {
                // 부족한 재고는 일반 재고로 계산한다.
                // 프로모션 가능한 개수

                // 부족한 수량 = 장바구니 - 계산된 프로모션 가용가능한 재고량(7개가 있어도 2+1 행사라면 6개만 가용 가능)
                int lowQuantity = cartItem.getQuantity() - availableQuantity;  // ex) 10 - 6 = 4
                outputView.printNotPromotionDiscount(cartItem.getName(), lowQuantity);
                String answer = inputAnswer();

                // 부족한 수량을 일반 재고로 계산
                if (Validate.isYesAnswer(answer)) {
                    Validate.validateEnoughStock(storage, cartItem);
                    int giftQuantity = promotion.calculateGiftQuantity(
                            cartItem.calculateAvailableQuantity(lowQuantity));
                    orderItems.add(new OrderItem(cartItem.getName(), cartItem.getQuantity(), giftQuantity,
                            promotionItem.getPrice()));
                    // TODO : 일반 상품 재고량 구매 수량과 비교
                    generalItem.updateQuantity(lowQuantity);
                    promotionItem.updateQuantity(cartItem.calculateAvailableQuantity(lowQuantity));
                }

                // 부족한 수량을 제외한다.(10-4 = 6)
                if (Validate.isNoAnswer(answer)) {
                    int giftQuantity = promotion.calculateGiftQuantity(
                            cartItem.calculateAvailableQuantity(lowQuantity));
                    cartItem.cancelItemQuantity(lowQuantity);
                    orderItems.add(new OrderItem(cartItem.getName(), cartItem.getQuantity(), giftQuantity,
                            promotionItem.getPrice()));
                    promotionItem.updateQuantity(cartItem.calculateAvailableQuantity(lowQuantity));
                }
                return;
            }
            // 프로모션 재고량이 충분한 경우

            // 프로모션 적용이 가능한데 해당 수량보다 적게 사는 경우
            if (!promotion.isMeetPromotionCondition(cartItem.getQuantity())) {
                outputView.printAdditionalGiftQuantity(cartItem.getName(), promotion.getGiftAmount());
                String answer = inputAnswer();
                if (Validate.isYesAnswer(answer)) {
                    cartItem.addGiftQuantity(promotion.getGiftAmount());
                }
            }

            int giftQuantity = promotion.calculateGiftQuantity(cartItem.getQuantity());
            orderItems.add(
                    new OrderItem(cartItem.getName(), cartItem.getQuantity(), giftQuantity, promotionItem.getPrice()));
            promotionItem.updateQuantity(cartItem.getQuantity());
        }
    }

    private String inputAnswer() {
        return Task.reTryTaskUntilSuccessful(() -> {
            String answer = inputView.inputAnswer();
            Validate.validateAnswer(answer);
            return answer;
        });
    }

    private String[] inputItem() {
        String[] items = inputView.inputItem().split(",", 0);
        Validate.validateItem(items);
        return items;
    }


    public static void printReceipt(Order order) {
        int maxItemNameLength = order.getOrderItems().stream()
                .mapToInt(item -> item.getName().length())
                .max()
                .orElse(1);

        System.out.println("==============W 편의점================");
        System.out.printf("%-" + maxItemNameLength + "s\t%10s\t%10s%n", "상품명", "수량", "금액");

        for (OrderItem orderItem : order.getOrderItems()) {
            System.out.printf("%-" + maxItemNameLength + "s\t%10d\t%,10d%n", orderItem.getName(),
                    orderItem.getPurchaseQuantity(), orderItem.getTotalPrice());
        }

        System.out.println("=============증    정===============");
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getPromotionGiftQuantity()) {
                System.out.printf("%-" + maxItemNameLength + "s\t%10d%n", orderItem.getName(),
                        orderItem.getPromotionQuantity());
            }
        }

        System.out.println("====================================");
        System.out.printf("%-" + maxItemNameLength + "s\t%10d\t%,10d%n", "총구매액", order.getTotalOrderQuantity(),
                order.getTotalOrderPrice());
        System.out.printf("%-" + maxItemNameLength + "s\t\t\t\t%,10d%n", "행사할인",
                -order.getTotalOrderPromotionDiscount());
        System.out.printf("%-" + maxItemNameLength + "s\t\t\t\t%,10d%n", "멤버십할인", -order.getMembershipDisCount());
        System.out.printf("%-" + maxItemNameLength + "s\t\t\t\t%,10d%n", "내실돈", order.getActualTotalPrice());
    }
}
