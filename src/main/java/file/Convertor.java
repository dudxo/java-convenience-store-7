package file;

import domain.Item;
import domain.Promotion;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Convertor {

    public static Item convertToItemFromData(String[] datas) {
        String name = datas[0].trim();
        int price = Integer.parseInt(datas[1].trim());
        int quantity = Integer.parseInt(datas[2].trim());
        String promotion = datas[3].trim();

        return new Item(name, price, promotion, quantity);
    }

    public static Promotion convertToPromotionFromData(String[] datas) {
        String promotion = datas[0].trim();
        int purchaseAmount = Integer.parseInt(datas[1].trim());
        int giftAmount = Integer.parseInt(datas[2].trim());
        LocalDate startDate = LocalDate.parse(datas[3].trim());
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 기본 시간 00:00:00

        LocalDate endDate = LocalDate.parse(datas[4].trim());
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 기본 시간 23:59:59 (마지막 시간)
        return new Promotion(promotion, purchaseAmount, giftAmount, startDateTime, endDateTime);
    }
}
