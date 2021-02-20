package util.parse;

/**
 * Gives a formal parser class for parsing to check if a given string is present at the start of the text for
 * removal. It doesn't have much novel functionality except for making existing code cleaner.
 */
class SequenceParser implements Parser<String> {
    String startString;
    private int parseCount;

    private SequenceParser() {}

    public SequenceParser(String startString) {
        this();
        this.startString = startString;
    }

    public String parse(String text) {
        parseCount = 0;
        if ( text.length() < startString.length() )
            return null;
        else if ( text.substring(0, startString.length()).equals(startString) ) {
            parseCount = startString.length();
            return startString;
        }
        return null;
    }

    public int getParsedLength() {
      return parseCount;
    }
}
