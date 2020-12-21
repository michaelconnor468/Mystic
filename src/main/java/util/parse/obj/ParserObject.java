package util.parse.obj;

/**
 * Wrapper class provides interface to objects produced by parser.
 */
public interface ParserObject {
    public enum ObjectType {STRING, BLOCK, ARRAY, INT, DOUBLE, PROPERTY}
    public ObjectType getType();
    public String toJSON();
}
