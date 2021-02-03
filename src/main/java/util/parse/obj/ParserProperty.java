package util.parse.obj;

/**
 * Represents a JSON property. Main upper storage class of all parser objects as all data in JSON is assosiated with a
 * named property.
 */
public class ParserProperty implements ParserObject {
    protected String name;
    protected ObjectType propertyType;
    protected ParserObject content;

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

    public void setName(String name) {this.name = name;}
    public void setPropertyType(ObjectType propertyType) {this.propertyType = propertyType;}
    public void setContent(ParserObject content) {this.content = content;}

	public String toJSON() {
		return name + ": " + content.toJSON();
	}
}

