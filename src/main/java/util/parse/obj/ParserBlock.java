package util.parse.obj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Higher level class that should be used as a wrapper for any JSON parse objects being written and read,
 * decouples users from any underlying structure of a file being parsed by placing everything in a neat little programatic box.
 *
 * Also useful for representing JSON objects of course.
 */
public class ParserBlock implements ParserObject {
	private HashMap<String, ParserObject> properties;

	public ParserBlock() { this.properties = new HashMap<>(); }

	public ObjectType getType() { return ObjectType.BLOCK; }
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
        sb.append("{");
        for ( String obj : properties.keySet() ) {
            sb.append(obj);
            sb.append(": ");
            sb.append(properties.get(obj).toJSON());
            sb.append(", ");
        }
        if ( properties.values().size() > 0 ) sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
	}

	public void addProperty(ParserProperty p) { properties.put(p.getName(), p.getContent()); }
    public ParserObject getProperty(String str) { return properties.get(str); }
    public HashMap<String, ParserObject> getProperties() { return properties; }
    public String toString() { return toJSON(); }
}
