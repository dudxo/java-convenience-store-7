package view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public String inputItem() {
        return getInput(ViewMessage.INPUT_PURCHASE_ITEMS_MSG.getMessage());
    }

    public String inputAnswer() {
        return Console.readLine().trim();
    }

    public String inputAdditionalPurchase() {
        return getInput(ViewMessage.INPUT_ADDITION_PURCHASE_MSG.getMessage());
    }

    private String getInput(String message) {
        System.out.printf(message);
        return Console.readLine().trim();
    }
}
