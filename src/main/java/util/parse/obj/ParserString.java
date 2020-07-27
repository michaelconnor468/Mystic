package util.parse.obj;

/**
 * Wrapper for a string parser class
 */
public class ParserString implements ParserObject {
    protected String string;

    private ParserString() {}
    public ParserString(String string) {this(); this.string = string;}
    public String getString() {return string;}
}