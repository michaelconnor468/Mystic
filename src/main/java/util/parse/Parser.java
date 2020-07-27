package util.parse;

public interface Parser<T> {
    public T parse(String text);
}