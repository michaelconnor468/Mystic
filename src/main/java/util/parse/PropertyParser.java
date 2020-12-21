package util.parse;

import util.parse.obj.*;

public class PropertyParser implements Parser<ParserProperty> {
    public ParserProperty parse(String text) {
        ParserProperty property = new ParserProperty();
        text = parseName(property, text);
        if ( text == null )
            return null;

        String next = (new SequenceParser(":")).parse(text);
        if ( next == null )
            return null;
        text = text.substring(next.length());
        text = cutWhitespace(text);
        if ( text.length() < 1 )
            return null;

        ParserObject content = (new CaseParser()).parse(text);
        if ( content == null )
            return null;
        property.setPropertyType(content.getType());
        property.setContent(content);
        return property;
    }

    private String cutWhitespace(String text) {
        if ( text == "" ) {
            return text;
        }
        String whitespace = (new WhitespaceParser()).parse(text);
        if ( whitespace == null )
            return text;
        return text.substring(whitespace.length());
    }

    private String parseName(ParserProperty property, String text) {
        String parsedText = (new PropertyNameParser()).parse(text);
        if ( parsedText == null )
            return null;
        property.setName(parsedText);
        text = text.substring(parsedText.length());
        text = cutWhitespace(text);
        return text;
    }
}
