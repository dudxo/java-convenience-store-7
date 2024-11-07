package file;

import domain.Item;
import domain.Promotion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private static final String ITEMS_FILE_PATH = "products.md";
    private static final String PROMOTIONS_FILE_PATH = "products.md";

    public static List<Item> loadProductsFromFile() {
        return loadFromFile(ITEMS_FILE_PATH, FileLoader::parseProductData);
    }

    public static List<Promotion> loadPromotionsFromFile() {
        return loadFromFile(PROMOTIONS_FILE_PATH, FileLoader::parsePromotionData);
    }

    private static <T> List<T> loadFromFile(String filePath, LineParser<T> parser) {
        List<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(getInputStreamReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                items.add(parser.parse(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private static Item parseProductData(String line) {
        String[] values = line.split(",", 0);
        String name = values[0].trim();
        int price = Integer.parseInt(values[1].trim());
        int quantity = Integer.parseInt(values[2].trim());
        String promotion = values[3].trim();
        return new Item(name, price, promotion, quantity);
    }

    private static Promotion parsePromotionData(String line) {
        String[] values = line.split(",", 0);
        String promotion = values[0].trim();
        int purchaseAmount = Integer.parseInt(values[1].trim());
        int giftAmount = Integer.parseInt(values[2].trim());
        LocalDate startDate = LocalDate.parse(values[3].trim());
        LocalDate endDate = LocalDate.parse(values[4].trim());
        return new Promotion(promotion, purchaseAmount, giftAmount, startDate, endDate);
    }

    private static InputStreamReader getInputStreamReader(String path) {
        return new InputStreamReader(getResourceAsStream(path));
    }

    private static InputStream getResourceAsStream(String path) {
        return FileLoader.class.getClassLoader().getResourceAsStream(path);
    }
}
