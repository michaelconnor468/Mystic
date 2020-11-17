package util.parse;

import util.parse.obj.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Parses a number that can be of type int or double. Both parsing functionalities were merged into one parser
 * class to consolidate more complex lookahead functionality, and to make use of predefined wrapper class' parsing.
 */
public class NumberParser implements Parser<ParserNumber> {
    public ParserNumber parse(String text) {
        Pattern pattern = Pattern.compile("^\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(text);
        if ( !matcher.find() ) {
            pattern = Pattern.compile("^\\d+");
            matcher = pattern.matcher(text);
            if (!matcher.find()) return null;
            return new ParserInt(Integer.parseInt(matcher.group()));
        }
        return new ParserDouble(Double.parseDouble(matcher.group()));
    }
}