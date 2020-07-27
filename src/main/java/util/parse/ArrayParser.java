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
    ParserArray array;
    ParserObject.ObjectType type;

    public ParserArray parse(String text) {
        if ( !( text.charAt(0) == '[' ) && ( text.charAt(text.length()) == ']' ) )
            return null;
        text = text.substring(1, text.length() - 1).trim();
        array = new ParserArray(ObjectType.STRING);
        if ( text.length() < 1 )
            return array;

        if ( (( text.charAt(0) == '\'' || text.charAt(0) != '"' ) && ( text.charAt(text.length() - 1) == '\'' || text.charAt(text.length() - 1) == '"' ) && ( text.charAt(0) == text.charAt(text.length() - 1) )) )
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
            return null;
        
        while ( text != "" ) {
            text = text.trim();
            CaseParser caseParser = new CaseParser(type);
            ParserObject parsedObject = caseParser.parse(text);
            if ( parsedObject == null )
                return null;
            text = text.substring(caseParser.getParsedText().length());
            array.add(parsedObject);
            text = text.trim();
            if ( text.length() < 1 )
                continue;
            if ( text.charAt(0) == ',' )
                text = text.substring(1);
        }

        return array;
    }
}