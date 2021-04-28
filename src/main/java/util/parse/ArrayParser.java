package util.parse;

import javax.lang.model.util.ElementScanner14;
import java.util.ArrayList;

import util.parse.obj.*;
import util.parse.obj.ParserObject.ObjectType;

/**
 * Parses a JSON array type. Enforces a single type of object that must be stored within the array or else returns null. This is due to the array needing to be easily
 * used without extraneuos type checking needed leading to a more robust interface.
 */
class ArrayParser implements Parser<ParserArray> {
    private int parsedLength;
    ParserArray array;
    ParserObject.ObjectType type;

    public ParserArray parse(String text) {
        parsedLength = 0;

        String prepText = prepareToParse(text);
        if ( prepText == null ) {
            System.err.printf("\nFailed to prepare text for parsing in ArrayParser. Started with:\n%s\n", startOf(text));
            return null;
        }

        text = cutWhitespace(prepText);
        while ( !text.equals("") ) {
            CaseParser caseParser = new CaseParser(type);
            ParserObject parsedObject = caseParser.parse(text);
            if ( parsedObject == null ) {
                parsedLength = 0;
                System.err.printf("\nFailed to parse array element starting with:\n%s\n", startOf(text));
                return null;
            }
            text = cutWhitespace(text.substring(caseParser.getParsedText().length()));
            array.add(parsedObject);
            parsedLength = parsedLength + caseParser.getParsedLength();

            if ( text.length() < 1 )
                continue;
            if ( text.charAt(0) == ',' ) {
                text = text.substring(1);
                parsedLength = parsedLength + 1;
            }

            text = cutWhitespace(text);
        }

        return array;
    }

    /**
     * Gets start of string for error reporting.
     */
    private String startOf(String text) {
        return text.substring(0, Math.min(text.length(), 200));
    }

    /**
     * Type of empty array defaults to STRING as it avoids the need to implement a more hack-y and difficult to use None type
     * in ParserArray
     */
    private void setTypeToParse(String text) {
        if ( text.length() == 0 || text.charAt(0) == '\'' || text.charAt(0) == '"' )
            type = ObjectType.STRING;
        else if ( text.charAt(0) == '[' && text.charAt(text.length() - 1) == ']' )
            type = ObjectType.ARRAY;
        else if ( text.charAt(0) == '{' && text.charAt(text.length() - 1) == '}' )
            type = ObjectType.BLOCK;
        else if ( (new NumberParser()).parse(text) != null && (new NumberParser()).parse(text) instanceof ParserInt )
            type = ObjectType.INT;
        else if ( (new NumberParser()).parse(text) != null && (new NumberParser()).parse(text) instanceof ParserDouble )
            type = ObjectType.DOUBLE;
        else
            type = null;
    }

    /**
     * Cuts whitespace in array and increments parsedLength accordingly
     */
    private String cutWhitespace( String text ) {
        WhitespaceParser whitespaceParser = new WhitespaceParser();
        String whitespace = whitespaceParser.parse(text);
        text = whitespace == null ? text : text.substring(whitespace.length());
        parsedLength = parsedLength + whitespaceParser.getParsedLength();
        return text;
    }

    /**
     * Prepares the string from parsing by removing all the syntatic boilerplate and setting required variables accoringly
     */
    private String prepareToParse(String text) {
        text = (new BracketedExpressionParser()).parse(text);
        if ( text == null || text.length() < 1 || text.charAt(0) != '[' || text.charAt(text.length() - 1) != ']' )
            return null;
        text = text.substring(1, text.length() - 1).trim();
        parsedLength = parsedLength + 2;
        setTypeToParse(text);
        array = new ParserArray(type);
        if ( text.length() < 1 )
            return text;

        if ( type == null ) {
            parsedLength = 0;
            return null;
        }
        return text;
    }

    public int getParsedLength() { return parsedLength; }
    public String toString() { return array.toString(); }
}
