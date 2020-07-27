package util.parse.obj;

import java.util.ArrayList;
import java.util.Iterator;

public class ParserArray implements ParserObject {
    private ObjectType arrayType;
    private ArrayList<ParserObject> array;
    private int length;

    private ParserArray() {
        length = 0;
        array = new ArrayList<ParserObject>();
    }

    public ParserArray( ObjectType type ) {
        this();
        this.arrayType = type;
    }

    public void add( ParserObject obj ) {
        length++;
        array.add(obj);
    }

    public Iterator getIterator() {
        return array.iterator();
    }
    public int getLength() { return length; }
    public ObjectType getElementType() {
        return arrayType;
    }

    public ParserObject getIndex(int index) {
        if ( index > length - 1 )
            return null;
        return array.get(index);
    }

    public ObjectType getType() { return ObjectType.ARRAY; }
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for ( ParserObject obj : array ) {
            sb.append(obj.toJSON());
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}