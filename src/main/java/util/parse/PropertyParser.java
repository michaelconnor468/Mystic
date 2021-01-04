package util.parse;

import util.parse.obj.*;

public class PropertyParser implements Parser<ParserProperty> {
    private int parsedLength;

    public ParserProperty parse(String text) {
        parsedLength = 0;
        ParserProperty property = new ParserProperty();
        text = parseName(property, text);
        if ( text == null ) {
            parsedLength = 0;
            return null;
        }

        SequenceParser colonParser = new SequenceParser(":");
        String next = colonParser.parse(text);
        if ( next == null ) {
            parsedLength = 0;
            return null;
        }

        parsedLength = parsedLength + colonParser.getParsedLength();
        text = text.substring(next.length());
        WhitespaceParser whitespaceParser = new WhitespaceParser();
        text = whitespaceParser.cutWhitespace(text);
        parsedLength = parsedLength + whitespaceParser.getParsedLength();
        if ( text.length() < 1 ) {
            parsedLength = 0;
            return null;
        }

        CaseParser caseParser = new CaseParser();
        ParserObject content = caseParser.parse(text);
        if ( content == null ) {
            parsedLength = 0;
            return null;
        }

        property.setPropertyType(content.getType());
        property.setContent(content);
        parsedLength = parsedLength + caseParser.getParsedLength();
        return property;
    }

    public int getParsedLength() {
      return parsedLength;
    }

    /**
     *  Helper method for parse
     */
    private String parseName(ParserProperty property, String text) {
        PropertyNameParser nameParser = new PropertyNameParser();
        String parsedText = nameParser.parse(text);
        if ( parsedText == null )
            return null;
        property.setName(parsedText);
        text = text.substring(nameParser.getParsedLength());
        parsedLength = parsedLength + nameParser.getParsedLength();
        WhitespaceParser whitespaceParser = new WhitespaceParser();
        text = whitespaceParser.cutWhitespace(text);
        parsedLength = parsedLength + whitespaceParser.getParsedLength();
        return text;
    }
}
