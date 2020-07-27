package util.parse;

import util.parse.obj.*;

/**
 * Utility parser that implements parsing logic for all object types and follows the logic of the type currently set.
 * Class created as a refactor solution to repeated switch statements throughout the code.
 */
public class CaseParser implements Parser<ParserObject> {
	private ParserObject.ObjectType type;
	private String parsedText;

	private CaseParser() { parsedText = ""; }
	public CaseParser(ParserObject.ObjectType type) {
		this();
		this.type = type;
	}

	public ParserObject parse(String text) {
		StringBuilder sb = new StringBuilder();
		switch ( type ) {
			case STRING:
				parsedText = (new BracketedExpressionParser()).parse(text);
				return parsedText == null ? null : new ParserString(parsedText.substring(0, parsedText.length() - 1 ));
			case INT:
			case DOUBLE:
				ParserNumber parserNumber = (new NumberParser()).parse(text);
				if ( parserNumber == null ) return null;
				parsedText = parserNumber.toJSON();
				return parserNumber.getType() == type ? parserNumber : null;
			case ARRAY:
				parsedText = (new BracketedExpressionParser()).parse(text);
				return parsedText == null ? null : (new ArrayParser()).parse(parsedText);
			case BLOCK:
				parsedText = (new BracketedExpressionParser()).parse(text);
				return parsedText == null ? null : (new BlockParser()).parse(parsedText);
			default:
				return null;
		}
	}

	public String getParsedText() { return parsedText; }
}
