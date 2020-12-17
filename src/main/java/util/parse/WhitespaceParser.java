package util.parse;

/**
 * This is a helper class that is often used throughout the parsers to simplify the logic of removing non-semantic
 * white space from the JSON
 */
public class WhitespaceParser implements Parser<String> {
    public String parse(String text) {
        if ( text.length() < 1 || !Character.isWhitespace(text.charAt(0)) ) 
            return null;

        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < text.length() && Character.isWhitespace(text.charAt(i)); i++ )
            sb.append(text.charAt(i));
        return sb.toString();
    }

    static String cutWhitespace(String text) {
        return (new WhitespaceParser()).parse(text) == null ? text : text.substring((new WhitespaceParser()).parse(text).length());
    }
}
