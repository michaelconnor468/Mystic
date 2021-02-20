package util.parse;

/**
 * This is a helper class that is often used throughout the parsers to simplify the logic of removing non-semantic
 * white space from the JSON
 */
class WhitespaceParser implements Parser<String> {
    public int parsedLength = 0;

    public String parse(String text) {
        parsedLength = 0;
        if ( text.length() < 1 || !Character.isWhitespace(text.charAt(0)) ) 
            return null;

        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < text.length() && Character.isWhitespace(text.charAt(i)); i++ ) {
            sb.append(text.charAt(i));
            parsedLength++;
        }
        return sb.toString();
    }

    public String cutWhitespace(String text) {
        parsedLength = 0;
        String ret = (new WhitespaceParser()).parse(text) == null ? text : text.substring((new WhitespaceParser()).parse(text).length());
        parsedLength = text.length() - ret.length();
        return ret;
    }

    public int getParsedLength() {
      return parsedLength;
    }
}
