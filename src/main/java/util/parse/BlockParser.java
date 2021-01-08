package util.parse;

import util.parse.obj.*;

public class BlockParser implements Parser<ParserBlock> {  
    private int parsedLength;

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
            PropertyParser propertyParser = new PropertyParser();
            ParserProperty property = propertyParser.parse(text);
            if ( property == null ) {
                parsedLength = 0;
                return null;
            }
            block.addProperty(property);
            text = cutToNextProperty(text) == null ? text : cutToNextProperty(text);
            text = whitespaceParser.cutWhitespace(text);
            parsedLength = parsedLength + whitespaceParser.getParsedLength() + propertyParser.getParsedLength();
        }
        return block;
    }

    private String cutToNextProperty( String text ) {
        WhitespaceParser whitespaceParser = new WhitespaceParser();
        text = text.substring(whitespaceParser.parse(text).length());
        if ( text != null && text.length() > 0 && text.charAt(0) == ',' ) {
            parsedLength = whitespaceParser.getParsedLength() + 1 + parsedLength;
            return text.substring(1);
        }
        return null;
    }

    public int getParsedLength() {
      return parsedLength;
    }
}
