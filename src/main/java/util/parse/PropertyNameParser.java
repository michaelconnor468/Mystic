package util.parse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PropertyNameParser implements Parser<String> {
    public String parse(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*");
        Matcher matcher = pattern.matcher(text);
        if ( !matcher.find() )
            return null;
        return matcher.group();
    }
}