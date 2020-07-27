package util.parse.obj;

import java.util.ArrayList;
import java.util.HashMap;

public class ParserBlock implements ParserObject {
	private ArrayList<ParserObject> properties;

	public ParserBlock() { this.properties = new ArrayList<>(); }

	public ObjectType getType() { return ObjectType.BLOCK; }
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
        sb.append("{");
        for ( ParserObject obj : properties ) {
            sb.append(obj.toJSON());
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
	}
}