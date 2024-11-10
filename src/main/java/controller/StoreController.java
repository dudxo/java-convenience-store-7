package controller;

import static view.Tag.ITEM_NAME;
import static view.Tag.ITEM_PRICE;
import static view.Tag.ITEM_QUANTITY;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<Item, Integer> itemStockChanges;

    private List<OrderItem> orderItems;


    public StoreController(DependencyFactory factory) {
        this.inputView = factory.getInputView();
        this.outputView = factory.getOutputView();
    }

    public void run() {
        initStore();

        while (true) {
            printStartMessage();
            Order order = playStore();
            printReceipt(order);
            if (Validate.isNoAnswer(inputAdditionalPurchase())) {
                break;
            }
        }
    }

    private String inputAdditionalPurchase() {
        return Task.reTryTaskUntilSuccessful(() -> {
            String answer = inputView.inputAdditionalPurchase();
            Validate.validateAnswer(answer);
            return answer;
        });
    }

    private void initStore() {
        this.storage = initStorage();
        this.promotions = initPromotions();
    }

    private Order playStore() {
        return Task.reTryTaskUntilSuccessful(() -> {
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
        List<CartItem> cartItems = getCartItem();
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

    private void updateStock(Map<Item, Integer> itemStockChanges) {
        for (Map.Entry<Item, Integer> entry : itemStockChanges.entrySet()) {
            Item item = entry.getKey();
            int quantityToDeduct = entry.getValue();
            item.updateQuantity(quantityToDeduct); // 재고 차감
        }
    }

    public Order initOrder(Cart cart) {
        initOrderItems(cart);
        int membershipDiscount = calculateMembershipDiscount();
        return new Order(orderItems, membershipDiscount);
    }

    private int calculateMembershipDiscount() {
        int totalGeneralPriceSum = 0;
        if (Validate.isNoAnswer(requestNtMembershipDiscount())) {
            return totalGeneralPriceSum;
        }

        totalGeneralPriceSum = calculateTotalGeneralPriceSum(totalGeneralPriceSum);
        return calculateTotalMembershipDiscount(totalGeneralPriceSum);
    }

    private int calculateTotalGeneralPriceSum(int totalGeneralPriceSum) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.isPromotionGiftQuantity()) {
                Promotion promotion = getPromotion(orderItem.getName());
                totalGeneralPriceSum += orderItem.calculateTotalGeneralPrice(promotion);
                continue;
            }
            totalGeneralPriceSum += orderItem.getTotalPrice();
        }
        return totalGeneralPriceSum;
    }

    private int calculateTotalMembershipDiscount(int totalGeneralPriceSum) {
        int membershipDiscount = (int) (totalGeneralPriceSum * 0.3);
        if (!validateMembershipDiscountLimit(membershipDiscount)) {
            membershipDiscount = 8000;
        }
        return membershipDiscount;
    }

    private boolean validateMembershipDiscountLimit(int membershipDiscount) {
        return membershipDiscount <= 8000;
    }

    private String requestNtMembershipDiscount() {
        outputView.printMembership();
        return Task.reTryTaskUntilSuccessful(() -> inputAnswer());
    }

    private void initOrderItems(Cart cart) {
        orderItems = new ArrayList<>();
        itemStockChanges = new HashMap<>();

        for (CartItem cartItem : cart.getCartItems()) {
            // 프로모션 상품
            if (isPromotion(cartItem)) {
                handlePromotionItem(cartItem);
                continue;
            }

            // 프로모션이 아닐때
            // 일반 상품 재고량 구매 수량과 비교
            handleGeneralItem(cartItem);
        }

        updateStock(itemStockChanges);
    }

    private void handleGeneralItem(CartItem cartItem) {
        Validate.validateEnoughStock(storage, cartItem);
        Item generalItem = storage.findGeneralItem(cartItem.getName());
        orderItems.add(new OrderItem(cartItem.getName(), cartItem.getQuantity(), 0, generalItem.getPrice()));
        itemStockChanges.put(generalItem, itemStockChanges.getOrDefault(generalItem, 0) + cartItem.getQuantity());
    }

    private boolean isPromotion(CartItem cartItem) {
        if (!isPromotionItem(cartItem)) {
            return false;
        }

        Promotion promotion = getPromotion(cartItem.getName());

        return promotion.isCurrentPromotion(DateTimes.now());
    }

    private boolean isPromotionItem(CartItem cartItem) {
        return storage.isPromotion(cartItem.getName());
    }

    private Promotion getPromotion(String itemName) {
        Item promotionItem = storage.findPromotionItem(itemName);
        String promotionDetail = promotionItem.getPromotionDetail();
        return promotions.getPromotion(promotionDetail);
    }

    private void handlePromotionItem(CartItem cartItem) {
        Item promotionItem = storage.findPromotionItem(cartItem.getName());

        Promotion promotion = getPromotion(cartItem.getName());
        int availableQuantity = promotionItem.availableQuantity(promotion.getTotalPromotionQuantity());

        // 구매 수량보다 프로모션 재고량이 부족한 경우
        if (isInsufficientPromotionStock(cartItem, availableQuantity)) {
            int lowQuantity = cartItem.calculateLowQuantity(availableQuantity);  // ex) 10 - 6 = 4
            handleInsufficientPromotionStock(cartItem, lowQuantity, promotion, promotionItem);
            return;
        }
        // 프로모션 재고량이 충분한 경우
        // 프로모션 적용이 가능한데 해당 수량보다 적게 사는 경우
        if (promotion.isPromotionCondition(cartItem.getQuantity())) {
            requestAdditionalGiftQuantity(cartItem, promotion);
        }

        applyPromotion(cartItem, promotionItem, promotion);
    }

    private boolean isInsufficientPromotionStock(CartItem cartItem, int availableQuantity) {
        return availableQuantity < cartItem.getQuantity();
    }

    private void applyPromotion(CartItem cartItem, Item promotionItem, Promotion promotion) {
        int giftQuantity = promotion.calculateGiftQuantity(cartItem.getQuantity());
        orderItems.add(createOrderItem(cartItem, promotionItem, giftQuantity));
        writeStockChange(itemStockChanges, promotionItem, cartItem.getQuantity());
    }

    private void requestAdditionalGiftQuantity(CartItem cartItem, Promotion promotion) {
        outputView.printAdditionalGiftQuantity(cartItem.getName(), promotion.getGiftAmount());
        if (Validate.isYesAnswer(inputAnswer())) {
            cartItem.addGiftQuantity(promotion.getGiftAmount());
        }
    }

    private void handleInsufficientPromotionStock(CartItem cartItem, int lowQuantity, Promotion promotion,
                                                  Item promotionItem) {
        // 부족한 재고는 일반 재고로 계산한다.
        // 부족한 수량 = 장바구니 - 계산된 프로모션 가용가능한 재고량(7개가 있어도 2+1 행사라면 6개만 가용 가능)
        String answer = requestChangeGeneralStock(cartItem, lowQuantity);

        // 부족한 수량을 일반 재고로 계산
        if (Validate.isYesAnswer(answer)) {
            // 일반 재고량이 부족한 수량보다 같거나 많은지 검증
            Validate.validateEnoughStock(storage, cartItem.getName(), lowQuantity);
            changeGeneralStock(cartItem, orderItems, promotion, promotionItem, lowQuantity, itemStockChanges);
        }

        // 부족한 수량을 제외한다.(10-4 = 6)
        if (Validate.isNoAnswer(answer)) {
            adjustForLowQuantity(cartItem, lowQuantity, promotion, promotionItem);
        }
    }

    private void adjustForLowQuantity(CartItem cartItem, int lowQuantity, Promotion promotion, Item promotionItem) {
        int giftQuantity = promotion.calculateGiftQuantity(cartItem.calculateAvailableQuantity(lowQuantity));
        Validate.validateEnoughStock(storage, cartItem.getName(), cartItem.getQuantity());
        cartItem.cancelItemQuantity(lowQuantity);
        orderItems.add(createOrderItem(cartItem, promotionItem, giftQuantity));
        writeStockChange(itemStockChanges, promotionItem, cartItem.getQuantity());
    }

    private void writeStockChange(Map<Item, Integer> itemStockChanges, Item item, int DeductedQuantity) {
        itemStockChanges.put(item, itemStockChanges.getOrDefault(item, 0) + DeductedQuantity);
    }

    private void changeGeneralStock(CartItem cartItem, List<OrderItem> orderItems, Promotion promotion,
                                    Item promotionItem, int lowQuantity, Map<Item, Integer> itemStockChanges) {
        Item generalItem = storage.findGeneralItem(cartItem.getName());
        int giftQuantity = promotion.calculateGiftQuantity(cartItem.calculateAvailableQuantity(lowQuantity));
        orderItems.add(createOrderItem(cartItem, promotionItem, giftQuantity));
        writeStockChange(itemStockChanges, promotionItem, promotionItem.getQuantity());
        writeStockChange(itemStockChanges, generalItem, cartItem.getQuantity() - promotionItem.getQuantity());
    }

    private OrderItem createOrderItem(CartItem cartItem, Item promotionItem, int giftQuantity) {
        return new OrderItem(cartItem.getName(), cartItem.getQuantity(), giftQuantity,
                promotionItem.getPrice());
    }

    private String requestChangeGeneralStock(CartItem cartItem, int lowQuantity) {
        outputView.printNotPromotionDiscount(cartItem.getName(), lowQuantity);
        String answer = inputAnswer();
        return answer;
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

    public void printReceipt(Order order) {
        int maxItemNameLength = order.getMaxItemNameLength();
        printReceiptHeader(maxItemNameLength);
        printPurchaseItems(order, maxItemNameLength);

        printGiftHeader();
        printGiftItems(order, maxItemNameLength);

        printReceiptResult(order);
    }

    private void printReceiptResult(Order order) {
        outputView.printResultLine();
        outputView.printTotalPurchasePrice(order);
        outputView.printTotalPromotionDiscount(order);
        outputView.printTotalMembershipDiscount(order);
        outputView.printActualPrice(order);
    }

    private void printGiftItems(Order order, int maxItemNameLength) {
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.isPromotionGiftQuantity()) {
                outputView.printGiftItemLine(maxItemNameLength, orderItem);
            }
        }
    }

    private void printGiftHeader() {
        outputView.printGiftHeader();
    }

    private void printPurchaseItems(Order order, int maxItemNameLength) {
        for (OrderItem orderItem : order.getOrderItems()) {
            outputView.printPurchaseItemLine(maxItemNameLength, orderItem);
        }
    }

    private void printReceiptHeader(int maxItemNameLength) {
        outputView.printReceiptHeader();
        outputView.printReceiptTag(maxItemNameLength, ITEM_NAME.getTitle(), ITEM_QUANTITY.getTitle(),
                ITEM_PRICE.getTitle());
    }
}
