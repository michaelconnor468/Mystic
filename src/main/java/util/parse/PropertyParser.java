package util.parse;

import util.parse.obj.*;

public class PropertyParser implements Parser<ParserProperty> {
    String parsedText = "";

    public ParserProperty parse(String text) {
        ParserProperty property = new ParserProperty();
        String next = (new PropertyNameParser()).parse(text);
        if ( next == null ) 
            return null;
        property.setName(next);
        parsedText += next;
        text = text.substring(next.length());
        if ( text.length() < 1 )
            return null;
        text = cutWhitespace(text);

        next = (new SequenceParser(":")).parse(text);
        if ( next == null )
            return null;
        parsedText += next;
        text = text.substring(next.length());
        if ( text.length() < 1 )
            return null;
        text = cutWhitespace(text);
        if ( text.length() < 1 )
            return null;
        String bracketedContent;

        switch (text.charAt(0)) {
            case '[':
                property.setPropertyType(ParserObject.ObjectType.ARRAY);
                bracketedContent = (new BracketedExpressionParser()).parse(text);
                property.setContent( (new ArrayParser()).parse(bracketedContent));
                parsedText += bracketedContent;
                property.setParsedText(parsedText);
                return property;
            case '{':
                property.setPropertyType(ParserObject.ObjectType.BLOCK);
                bracketedContent = (new BracketedExpressionParser()).parse(text);
                property.setContent( (new BlockParser()).parse(bracketedContent));
                parsedText += bracketedContent;
                property.setParsedText(parsedText);
                return property;
            case '"':
                property.setPropertyType(ParserObject.ObjectType.STRING);
                bracketedContent = (new BracketedExpressionParser()).parse(text);
                property.setContent( new ParserString(bracketedContent.substring(1, bracketedContent.length() - 1)));
                parsedText += bracketedContent;
                property.setParsedText(parsedText);
                return property;
            case '\'':
                property.setPropertyType(ParserObject.ObjectType.STRING);
                bracketedContent = (new BracketedExpressionParser()).parse(text);
                property.setContent( new ParserString(bracketedContent.substring(1, bracketedContent.length() - 1)));
                parsedText += bracketedContent;
                property.setParsedText(parsedText);
                return property;
            default:
                if (Character.isDigit(text.charAt(0))) {
                    ParserNumber number = (new NumberParser()).parse(text);
                    if ( number instanceof ParserInt )
                        property.setPropertyType(ParserObject.ObjectType.INT);
                    else
                        property.setPropertyType(ParserObject.ObjectType.DOUBLE);
                    property.setContent(number);
                }
                else 
                    property = null;
                return property;
        }

        //return property;
    }

    private String cutWhitespace(String text) { 
        String whitespace = (new WhitespaceParser()).parse(text);
        if ( whitespace == null )
            return text;
        parsedText += whitespace; 
        return text.substring(whitespace.length());
    }
}