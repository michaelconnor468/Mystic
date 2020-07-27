package util.parse.obj;

import java.util.ArrayList;
import java.util.Iterator;

public class ParserArray implements ParserObject {
    private ObjectType arrayType;
    private ArrayList<ParserObject> array;
    private int length;

    public ParserArray() {
        length = 0;
        arrayType = null;
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

    public ObjectType getType() {
        return arrayType;
    }

    public ParserObject getIndex(int index) {
        if ( index > length - 1 )
            return null;
        return array.get(index);
    }
}