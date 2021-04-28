package util.parse;

import util.parse.obj.*;

/**
 * Top level parse used to parse the outer blocks of JSON data and JSON objects.
 */
public class BlockParser implements Parser<ParserBlock> {  
    private int parsedLength;
    private WhitespaceParser whitespaceParser;
    private ParserBlock block;

    public BlockParser() { whitespaceParser = new WhitespaceParser(); }

    public ParserBlock parse(String text) {
        parsedLength = 0;
        String prepText = prepareForParsing(text);
        if ( prepText == null ) {
            System.out.printf("\nFaile to prepare text for parsing in BLockParser. Started with: \n%s\n", startOf(text));
            return null;
        }
        text = prepText;
        block = new ParserBlock();

        // Iterates through all remaining properties and parses them accordingly
        while ( !text.equals("") ) {
            PropertyParser propertyParser = new PropertyParser();
            ParserProperty property = propertyParser.parse(text);
            if ( property == null ) {
                parsedLength = 0;
                System.out.printf("\nFailed to parse property in block starting with:\n%s\n", startOf(text));
                return null;
            }
            block.addProperty(property);

            text = text.substring(propertyParser.getParsedLength());
            String cutText = cutToNextProperty(text);
            text = cutText == null ? text : cutText;
            text = whitespaceParser.cutWhitespace(text);
            parsedLength = parsedLength + whitespaceParser.getParsedLength() + propertyParser.getParsedLength();
        }
        return block;
    }

    /**
     * Gets start of string for error reporting.
     */
    private String startOf(String text) {
        return text.substring(0, Math.min(text.length(), 200));
    }

    /**
     * Helper method to basically remove comma and whitespace before it between properties
     */
    private String cutToNextProperty( String text ) {
        text = whitespaceParser.cutWhitespace(text);
        if ( text != null && text.length() > 0 && text.charAt(0) == ',' ) {
            parsedLength = whitespaceParser.getParsedLength() + 1 + parsedLength;
            return text.substring(1);
        }
        return null;
    }
    
    /**
     * Handles parsing of non-property boilerplate syntax of the block and returns a string of comma separated properties
     *
     * increments parsedLength as it should but there is not much reason to worry about this
     */
    private String prepareForParsing(String text) {
        // Checks if string starts with brackers handled by bracketedexpressionparser
        text = (new BracketedExpressionParser()).parse(text);
        if ( text == null ) return null;
        text = text.substring(1, text.length() - 1);
        parsedLength = parsedLength + 2;
        
        text = whitespaceParser.cutWhitespace(text);
        parsedLength = parsedLength + whitespaceParser.parsedLength;
        return text;
    }

    public int getParsedLength() { return parsedLength; }
    public String toString() { return block.toString(); }
}
