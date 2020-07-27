package util.parse;

/**
 * Gives a formal parser class for parsing to check if a given string is present at the start of the text for
 * removal. It doesn't have much novel functionality except for making existing code cleaner.
 */
public class SequenceParser implements Parser<String> {
    String startString;

    private SequenceParser() {}

    public SequenceParser(String startString) {
        this();
        this.startString = startString;
    }

    public String parse(String text) {
        if ( text.length() < startString.length() )
            return null;
        else if ( text.substring(0, startString.length()).equals(startString) )
            return startString;
        return null;
    }
}