package domain;

import java.util.List;

public class Storage {

    private final List<Item> items;

    public Storage(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("- ").append(item.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
