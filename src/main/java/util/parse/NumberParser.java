package util.parse;

import util.parse.obj.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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