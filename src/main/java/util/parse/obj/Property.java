package util.parse.obj;

/**
 * Represents a JSON property
 */
public class Property implements ParserObject {
    protected String name;
    protected String parsedText;
    protected ObjectType propertyType;
    protected ParserObject content;

    public String getName() {return name;}
    public ObjectType getPropertyType() {return propertyType;}
    public ParserObject getContent() {return content;}

    public void setName(String name) {this.name = name;}
    public void setPropertyType(ObjectType propertyType) {this.propertyType = propertyType;}
    public void setContent(ParserObject content) {this.content = content;}
    public void setParsedText(String parsedText) {this.parsedText = parsedText;}
}

