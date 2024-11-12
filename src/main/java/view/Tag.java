package view;

public enum Tag {

    ITEM_NAME("상품명"),
    ITEM_QUANTITY("수량"),
    ITEM_PRICE("가격");

    private final String title;

    Tag(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
