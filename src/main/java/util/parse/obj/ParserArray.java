package util.parse.obj;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Object used by API to represent a parsed JSON array of a single type so as to keep logic simple
 */
public class ParserArray implements ParserObject, Iterable<ParserObject> {
    private ObjectType arrayType;
    private ArrayList<ParserObject> array;
    private int length;

    private ParserArray() {}
    public ParserArray( ObjectType type ) {
        length = 0;
        array = new ArrayList<ParserObject>();
        this.arrayType = type;
    }

    public void add( ParserObject obj ) {
        length++;
        array.add(obj);
    }

    public Iterator     iterator()       { return array.iterator();}
    public int          getLength()         { return length; }
    public ObjectType   getElementType()    { return arrayType;}
    public ParserObject getIndex(int index) { return index > length - 1 ? null : array.get(index);}
    public ObjectType   getType()           { return ObjectType.ARRAY; }
    
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for ( ParserObject obj : array ) {
            sb.append(obj.toJSON());
            sb.append(", ");
        }
        if ( array.size() > 0 ) sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    public String toString() {return toJSON();}
}
