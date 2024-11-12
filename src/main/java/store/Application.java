package store;

import controller.StoreController;
import util.DependencyFactory;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        DependencyFactory factory = new DependencyFactory();
        StoreController storeController = new StoreController(factory);
        storeController.run();
    }
}
