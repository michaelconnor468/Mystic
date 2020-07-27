package util.parse;

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