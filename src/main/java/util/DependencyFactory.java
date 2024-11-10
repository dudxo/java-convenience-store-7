package util;

import view.InputView;
import view.OutputView;

public class DependencyFactory {
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public InputView getInputView() {
        return inputView;
    }

    public OutputView getOutputView() {
        return outputView;
    }
}
