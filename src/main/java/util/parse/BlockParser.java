package util.parse;

import util.parse.obj.*;

public class BlockParser implements Parser<ParserBlock> {  
    public int parsedLength;

    public ParserBlock parse(String text) {
        parsedLength = 0;
        text = (new BracketedExpressionParser()).parse(text);
        if ( text == null )
            return null;
        text = text.substring(1, text.length() - 1);
        parsedLength = parsedLength + 2;
        WhitespaceParser whitespaceParser = new WhitespaceParser();
        text = whitespaceParser.cutWhitespace(text);
        parsedLength = parsedLength + whitespaceParser.parsedLength;

        ParserBlock block = new ParserBlock();

        while ( !text.equals("") ) {
            ParserProperty property = (new PropertyParser()).parse(text);
            if ( property == null )
                return null;
            block.addProperty(property);
            text = cutToNextProperty(text);
        }
        return block;
    }

    private String cutToNextProperty( String text ) {
        while ( text.length() > 0 ) {
            text = text.substring((new WhitespaceParser()).parse(text).length());
            if ( text != null && text.length() > 0 && text.charAt(0) == ',' )
                return text.substring(1);
        }
        return null;
    }

    public int getParsedLength() {
      return parsedLength;
    }
}
