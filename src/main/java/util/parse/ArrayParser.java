package util.parse;

import javax.lang.model.util.ElementScanner14;

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
        text = text.substring(1, text.length() - 1);
        array = new ParserArray();
        if ( text.length() < 1 )
            return array;
        String[] elements = text.split(",");
        
        String firstElement = elements[0].trim();
        if ( (( firstElement.charAt(0) == '\'' || firstElement.charAt(0) != '"' ) && ( firstElement.charAt(firstElement.length() - 1) == '\'' || firstElement.charAt(firstElement.length() - 1) == '"' ) && ( firstElement.charAt(0) == firstElement.charAt(firstElement.length() - 1) )) ) 
            type = ObjectType.STRING;
        else if ( firstElement.charAt(0) == '[' && firstElement.charAt(firstElement.length() - 1) == ']' )
            type = ObjectType.ARRAY;
        else if ( firstElement.charAt(0) == '{' && firstElement.charAt(firstElement.length() - 1) == '}' )
            type = ObjectType.BLOCK;
        else if ( (new NumberParser()).parse(firstElement) != null && (new NumberParser()).parse(firstElement) instanceof ParserInt )
            type = ObjectType.INT;
        else if ( (new NumberParser()).parse(firstElement) != null && (new NumberParser()).parse(firstElement) instanceof ParserDouble )
            type = ObjectType.DOUBLE;
        else
            return null;
        
        for ( String x : elements ) {
            x = x.trim();
            switch (type) {
                case STRING:
                    if ( !(( x.charAt(0) == '\'' || x.charAt(0) != '"' ) && ( x.charAt(x.length() - 1) == '\'' || x.charAt(x.length() - 1) == '"' ) && ( x.charAt(0) == x.charAt(x.length() - 1) )) )
                        return null;
                    array.add( new ParserString(x.substring(1, x.length() - 1)) );
                    break;
                case ARRAY:
                    ParserArray parsedArray = (new ArrayParser()).parse(x);
                    if ( parsedArray == null )
                        return null;
                    array.add( parsedArray );
                    break;
                case BLOCK:
                    ParserBlock parsedBlock = (new BlockParser()).parse(x);
                    if ( parsedBlock == null )
                        return null;
                    array.add( parsedBlock );
                    break;
                case INT:
                    if ( (new NumberParser()).parse(x) == null || ((new NumberParser()).parse(x) instanceof ParserInt) )
                        return null;
                    array.add( (new NumberParser()).parse(x) );
                    break;
                case DOUBLE:
                    if ( (new NumberParser()).parse(x) == null || ((new NumberParser()).parse(x) instanceof ParserDouble) )
                    return null;
                    array.add( (new NumberParser()).parse(x) );
                    break;
            }
        }

        return array;
    }
}