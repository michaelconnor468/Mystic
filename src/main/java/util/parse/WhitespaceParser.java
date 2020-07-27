package util.parse;

public class WhitespaceParser implements Parser<String> {
    public String parse(String text) {
        if ( text.length() < 1 || !Character.isWhitespace(text.charAt(0)) ) 
            return null;

        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < text.length() && Character.isWhitespace(text.charAt(i)); i++ )
            sb.append(text.charAt(i));
        return sb.toString();
    }
}