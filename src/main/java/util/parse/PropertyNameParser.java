package util.parse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Used to parse the name of a property that comes before : in JSON. Since these are going to be mapped to java
 * variables within the program, the regex picks on any name suitable for the java language.
 */
class PropertyNameParser implements Parser<String> {
    private int parsedLength;
    public String parse(String text) {
        parsedLength = 0;
        Pattern pattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*");
        Matcher matcher = pattern.matcher(text);
        if ( !matcher.find() )
            return null;
        parsedLength = matcher.group().length();
        return matcher.group();
    }

    public int getParsedLength() { return parsedLength; }
}
