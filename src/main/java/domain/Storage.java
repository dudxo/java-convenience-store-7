package domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import valid.Validate;

public class Storage {

    private final List<Item> items;

    public Storage(List<Item> items) {
        validateItems(items);
        this.items = items;
    }

    private void validateItems(List<Item> items) {
        Map<String, Long> promotionCountForItem = getPromotionCountForItem(items);
        Validate.validateMultiplePromotion(promotionCountForItem);
    }

    private Map<String, Long> getPromotionCountForItem(List<Item> items) {
        return items.stream()
                .filter(Item::isPromotionItem)
                .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("- ").append(item.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public List<Item> getItems() {
        return List.copyOf(items);
    }

    public boolean isPromotion(String itemName) {
        return this.getItems().stream()
                .filter(item -> item.containsName(itemName))
                .anyMatch(Item::isPromotionItem);
    }

    public Item findPromotionItem(String itemName) {
        return this.getItems().stream()
                .filter(item -> item.containsName(itemName))
                .filter(Item::isPromotionItem) // 프로모션 상품인지 확인
                .findFirst()
                .orElse(null); // 찾지 못하면 null 반환
    }

    public Item findGeneralItem(String itemName) {
        return this.getItems().stream()
                .filter(item -> item.containsName(itemName))
                .filter(item -> !item.isPromotionItem())
                .findFirst()
                .orElse(null); // 찾지 못하면 null 반환
    }

    public boolean containsItem(String itemName) {
        return items.stream()
                .anyMatch(item -> item.containsName(itemName));
    }

    public boolean isEnoughQuantity(String itemName, int requiredQuantity) {
        return this.getItems().stream()
                .filter(item -> item.containsName(itemName))
                .filter(item -> !item.isPromotionItem())
                .anyMatch(item -> item.isEnoughQuantity(requiredQuantity));
    }

    public int getMaxItemNameLength() {
        return items.stream()
                .mapToInt(item -> item.getName().length())
                .max()
                .orElse(1);
    }
}
