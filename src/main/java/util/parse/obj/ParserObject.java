package util.parse.obj;

/**
 * Wrapper class provides interface to objects produced by parser.
 */
public interface ParserObject {
    enum ObjectType {STRING, BLOCK, ARRAY, INT, DOUBLE, PROPERTY}
    public abstract ObjectType getType();
    public abstract String toJSON();
}
