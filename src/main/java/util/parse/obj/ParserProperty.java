package util.parse.obj;

/**
 * Represents a JSON property. Main upper storage class of all parser objects as all data in JSON is assosiated with a
 * named property.
 */
public class ParserProperty implements ParserObject {
    protected String name;
    protected String parsedText;
    protected ObjectType propertyType;
    protected ParserObject content;

    public String getName() {return name;}
    public ObjectType getPropertyType() {return propertyType;}
    public ParserObject getContent() {return content;}
    public ObjectType getType() { return ObjectType.PROPERTY; }

    public void setName(String name) {this.name = name;}
    public void setPropertyType(ObjectType propertyType) {this.propertyType = propertyType;}
    public void setContent(ParserObject content) {this.content = content;}
    public void setParsedText(String parsedText) {this.parsedText = parsedText;}

	public String toJSON() {
		return name + ": " + content.toJSON();
	}
}

