package file;

public interface LineParser<T> {
    T parse(String line);
}
