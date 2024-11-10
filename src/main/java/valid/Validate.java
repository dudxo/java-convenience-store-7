package valid;

import domain.CartItem;
import domain.Storage;
import exception.ErrorMessage;

public class Validate {

    private static final String INPUT_PURCHASE_REGEX = "\\[([\\w가-힣]+)-(\\d+)\\]";

    public static void validateAnswer(String answer) {
        if (isYesAnswer(answer) || isNoAnswer(answer)) {
            return;
        }
        throw new IllegalArgumentException(ErrorMessage.INVALID_ANSWER_MSG.getMessage());
    }

    public static boolean isYesAnswer(String answer) {
        return "Y".equalsIgnoreCase(answer);
    }

    public static boolean isNoAnswer(String answer) {
        return "N".equalsIgnoreCase(answer);
    }


    public static void validateItem(String[] items) {
        for (String item : items) {
            if (isMatchItemRegex(item)) {
                continue;
            }
            throw new IllegalArgumentException(ErrorMessage.INVALID_PURCHASE_INPUT_MSG.getMessage());
        }
    }

    private static boolean isMatchItemRegex(String item) {
        return item.matches(INPUT_PURCHASE_REGEX);
    }

    public static void existItemNameInStorage(Storage storage, String itemName) {
        if (storage.containsItem(itemName)) {
            return;
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_FOUND_ITEM_MSG.getMessage());
    }

    public static void validateEnoughStock(Storage storage, CartItem cartItem) {
        if (storage.isEnoughQuantity(cartItem.getName(), cartItem.getQuantity())) {
            return;
        }
        throw new IllegalArgumentException(ErrorMessage.EXCEED_STOCK_MSG.getMessage());
    }

    public static void validateEnoughStock(Storage storage, String name, int quantity) {
        if (storage.isEnoughQuantity(name, quantity)) {
            return;
        }
        throw new IllegalArgumentException(ErrorMessage.EXCEED_STOCK_MSG.getMessage());
    }
}
