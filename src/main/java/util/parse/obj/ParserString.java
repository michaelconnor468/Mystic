package util.parse.obj;

/**
 * Wrapper for a string parser class
 */
public class ParserString implements ParserObject {
    private String string;
    private ParserString() {}
    
    public ParserString(String string) {this(); this.string = string;}
    public String getString() {return string;}
    public String toJSON() { return "\"" + string + "\""; }
    public ObjectType getType() { return ObjectType.STRING; }
    public String toString() { return toJSON(); }
}
