package util.parse;

import javax.lang.model.util.ElementScanner14;
import java.util.ArrayList;

import util.parse.obj.*;
import util.parse.obj.ParserObject.ObjectType;

/**
 * Parses a JSON array type. Enforces a single type of object that must be stored within the array or else returns null. This is due to the array needing to be easily
 * used without extraneuos type checking needed leading to a more robust interface.
 */
public class ArrayParser implements Parser<ParserArray> {
    private int parsedLength;
    ParserArray array;
    ParserObject.ObjectType type;

    public ParserArray parse(String text) {
        parsedLength = 0;
        BracketedExpressionParser bracketParser = new BracketedExpressionParser();
        text = bracketParser.parse(text);
        if ( text == null || text.length() < 1 || text.charAt(0) != '[' || text.charAt(text.length() - 1) != ']' )
            return null;
        text = text.substring(1, text.length() - 1).trim();
        parsedLength = parsedLength + 2;
        array = new ParserArray(ObjectType.STRING);
        if ( text.length() < 1 )
            return array;

        setTypeToParse(text);
        if ( type == null ) {
            parsedLength = 0;
            return null;
        }

        while ( !text.equals("") ) {
            WhitespaceParser whitespaceParser = new WhitespaceParser();
            String whitespace = whitespaceParser.parse(text);
            text = whitespace == null ? text : text.substring(whitespace.length());
            parsedLength = parsedLength + whitespaceParser.getParsedLength();
            if ( text.equals("") )
                continue;
            CaseParser caseParser = new CaseParser(type);
            ParserObject parsedObject = caseParser.parse(text);
            if ( parsedObject == null ) {
                parsedLength = 0;
                return null;
            }
            text = text.substring(caseParser.getParsedText().length());
            array.add(parsedObject);
            whitespace = whitespaceParser.parse(text);
            text = whitespace == null ? text : text.substring(whitespace.length());
            parsedLength = parsedLength + whitespaceParser.getParsedLength() + caseParser.getParsedLength();
            if ( text.length() < 1 )
                continue;
            if ( text.charAt(0) == ',' ) {
                text = text.substring(1);
                parsedLength = parsedLength + 1;
            }
        }

        return array;
    }

    private void setTypeToParse(String text) {
        if ( text.charAt(0) == '\'' || text.charAt(0) == '"' )
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

    public int getParsedLength() { return parsedLength; }
}
