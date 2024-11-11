package file;

import domain.Item;
import domain.Promotion;
import exception.ErrorMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private static final String ITEMS_FILE_PATH = "products.md";
    private static final String PROMOTIONS_FILE_PATH = "promotions.md";

    public static List<Item> loadProductsFromFile() {
        return loadFromFile(ITEMS_FILE_PATH, FileLoader::parseProductData);
    }

    public static List<Promotion> loadPromotionsFromFile() {
        return loadFromFile(PROMOTIONS_FILE_PATH, FileLoader::parsePromotionData);
    }

    private static <T> List<T> loadFromFile(String filePath, LineParser<T> parser) {
        List<T> entities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(getInputStreamReader(filePath))) {
            getEntitiesForLine(parser, reader, entities);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.NOT_READ_FILE.getMessage());
        }
        return entities;
    }

    private static <T> void getEntitiesForLine(LineParser<T> parser, BufferedReader reader, List<T> entities)
            throws IOException {
        String line;
        boolean isFirstLine = true;
        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            entities.add(parser.parse(line));
        }
    }

    private static Item parseProductData(String line) {
        String[] datas = line.split(",", 0);
        return Convertor.convertToItemFromData(datas);
    }

    private static Promotion parsePromotionData(String line) {
        String[] datas = line.split(",", 0);
        return Convertor.convertToPromotionFromData(datas);
    }

    private static InputStreamReader getInputStreamReader(String path) {
        return new InputStreamReader(getResourceAsStream(path));
    }

    private static InputStream getResourceAsStream(String path) {
        return FileLoader.class.getClassLoader().getResourceAsStream(path);
    }
}
