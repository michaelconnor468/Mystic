package util.parse;

import util.parse.obj.*;

/**
 * Utility parser that implements parsing logic for all object types and follows the logic of the type currently set.
 * Class created as a refactor solution to repeated switch statements throughout the code. Can be given a type in the
 * constructor to parse against for stronger checking. If type is unknown however, it will discern it using
 * discernType().
 *
 * Does not support parsing of the property case as it is not necessary or useful to and would further conplicate the
 * logic.
 */
class CaseParser implements Parser<ParserObject> {
	private ParserObject.ObjectType type;
	private String parsedText;
  private int parsedLength;

	public CaseParser() {
		parsedText = "";
		type = null;
	}
	
    public CaseParser(ParserObject.ObjectType type) {
		this();
		this.type = type;
	}

	public ParserObject parse(String text) {
		parsedLength = 0;
        type = type == null ? discernType(text) : type;
		if ( type == null )
			return null;

		StringBuilder sb = new StringBuilder();
        BracketedExpressionParser bracketParser = new BracketedExpressionParser();
		switch ( type ) {
			case STRING:
				parsedText = bracketParser.parse(text);
                parsedLength = parsedLength + bracketParser.getParsedLength();
				if ( parsedText == null || (parsedText.charAt(0) != '\'' && parsedText.charAt(0) != '"') ) {
                    parsedLength = 0;
                    return null;
                }
                else 
                    return new ParserString(parsedText.substring(1, parsedText.length() - 1));
			case INT:
			case DOUBLE:
                NumberParser numParser = new NumberParser();
			    ParserNumber parserNumber = numParser.parse(text);
			    if ( parserNumber == null ) return null;
			    parsedText = parserNumber.toJSON();
                parsedLength = numParser.getParsedLength();
			    return parserNumber.getType() == type ? parserNumber : null;
			case ARRAY:
				parsedText = bracketParser.parse(text);
                parsedLength = bracketParser.getParsedLength();
				return parsedText == null ? null : (new ArrayParser()).parse(parsedText);
			case BLOCK:
				parsedText = bracketParser.parse(text);
                parsedLength = bracketParser.getParsedLength();
				return parsedText == null ? null : (new BlockParser()).parse(parsedText);
			default:
				return null;
		}
	}

	private ParserObject.ObjectType discernType(String text) {
		switch ( text.charAt(0) ) {
			case '[':
				return ParserObject.ObjectType.ARRAY;
			case '{':
				return ParserObject.ObjectType.BLOCK;
			case '\"':
			case '\'':
				return ParserObject.ObjectType.STRING;
			default:
				ParserNumber number = (new NumberParser()).parse(text);
				if ( number instanceof ParserInt )
					return ParserObject.ObjectType.INT;
				else if ( number instanceof ParserDouble )
					return ParserObject.ObjectType.DOUBLE;
		}
        return null;
	}

	public String getParsedText() { return parsedText; }
    public int getParsedLength() { return parsedLength; }
}
