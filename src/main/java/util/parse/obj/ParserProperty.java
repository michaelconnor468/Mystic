package util.parse.obj;

/**
 * Represents a JSON property. Main upper storage class of all parser objects as all data in JSON is assosiated with a
 * named property.
 */
public class ParserProperty implements ParserObject {
    private String name;
    private ObjectType propertyType;
    private ParserObject content;

    public ParserProperty() {}
    public ParserProperty(String name, ParserObject content) {
        this.name = name;
        this.content = content;
        this.propertyType = content.getType();
    }
    public String getName() {return name;}
    public ObjectType getPropertyType() {return propertyType;}
    public ParserObject getContent() {return content;}
    public ObjectType getType() { return ObjectType.PROPERTY; }

    public ParserProperty setName(String name) {this.name = name; return this;}
    public ParserProperty setPropertyType(ObjectType propertyType) {this.propertyType = propertyType; return this;}
    public ParserProperty setContent(ParserObject content) {this.content = content; return this;}

	public String toJSON() { return name + ": " + content.toJSON();}
    public String toString() { return toJSON(); }
}

