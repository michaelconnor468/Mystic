package util.parse.obj;

import java.util.ArrayList;
import java.util.HashMap;

public class ParserBlock implements ParserObject {
	private HashMap<String, ParserObject> properties;

	public ParserBlock() { this.properties = new HashMap<>(); }

	public ObjectType getType() { return ObjectType.BLOCK; }
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
        sb.append("{");
        for ( ParserObject obj : properties.values() ) {
            sb.append(obj.toJSON());
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
	}

	public void addProperty(ParserProperty p) { properties.put(p.getName(), p.getContent()); }
  public ParserObject getProperty(String str) { return properties.get(str); }
	public HashMap<String, ParserObject> getProperties() { return properties; }
}
