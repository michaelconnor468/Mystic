package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ParserTests {


	@Test
	void SequenceParserTest() {
		SequenceParser parser = new SequenceParser("hello");
		String errormsg = "SequenceParser incorrectly parsed a substring at start of string";

		assertAll(
			() -> assertNull(parser.parse(" hello"), errormsg),
			() -> assertNull(parser.parse("he llo"), errormsg),
			() -> assertNull(parser.parse("he\nllo"), errormsg),
			() -> assertNull(parser.parse("_hello"), errormsg),
			() -> assertNull(parser.parse("abc"), errormsg),
			() -> assertNull(parser.parse("\nhello"), errormsg),
			() -> assertNull(parser.parse(""), errormsg),

			() -> assertEquals("hello", parser.parse("hello"), errormsg),
			() -> assertEquals("hello", parser.parse("hellohi"), errormsg),
			() -> assertEquals("hello", parser.parse("hello hi"), errormsg),
			() -> assertEquals("hello", parser.parse("hello\nhi"), errormsg)
		);
	}

	@Test
	void BracketParserTest() {
		BracketedExpressionParser parser = new BracketedExpressionParser();
		String errormsg = "Bracketed expression parser failed to correctly parse a bracketed string correctly";

		assertAll(
			() -> assertNull(parser.parse("'hello"), errormsg),
			() -> assertNull(parser.parse(" [ test ]"), errormsg),
			() -> assertNull(parser.parse(""), errormsg),
			() -> assertNull(parser.parse("[ hi "), errormsg),
			() -> assertNull(parser.parse("{"), errormsg),
			() -> assertNull(parser.parse("{ hello { hi }"), errormsg),
			() -> assertNull(parser.parse("\""), errormsg),
			() -> assertNull(parser.parse("\" \n"), errormsg),
			() -> assertNull(parser.parse("{ \n\t { \n }"), errormsg),

			() -> assertEquals("{test}", parser.parse("{test}"), errormsg),
			() -> assertEquals("{\n{test} test}", parser.parse("{\n{test} test}"), errormsg),
			() -> assertEquals("[test]", parser.parse("[test]"), errormsg),
			() -> assertEquals("(test)", parser.parse("(test)"), errormsg),
			() -> assertEquals("\"test\"", parser.parse("\"test\""), errormsg),
			() -> assertEquals("'test'", parser.parse("'test'"), errormsg)
		);
	}

	@Test
	void ArrayParserTest() {
		String errormsg = "Array parser failed to correctly parse an array";

		assertAll(
			() -> assertNull((new PropertyParser()).parse("'test']"), errormsg),
			() -> assertNull((new PropertyParser()).parse("['test'"), errormsg),
		    () -> assertNull((new PropertyParser()).parse(""), errormsg)
		);

		assertAll(
			() -> assertEquals("test1", ((ParserString) (new ArrayParser()).parse("['test1', 'test2']").getIndex(0)).getString(), errormsg),
			() -> assertEquals("test2", ((ParserString) (new ArrayParser()).parse("['test1', 'test2']").getIndex(1)).getString(), errormsg),
			() -> assertEquals("test1", ((ParserString) ((ParserArray) (new ArrayParser()).parse("[['test1', 'test3'], ['test2']]").getIndex(1)).getIndex(0)).getString(), errormsg),
			() -> assertEquals(2, (new ArrayParser()).parse("['test1', 'test2']").getLength(), errormsg),
			() -> assertEquals(0, (new ArrayParser()).parse("[]").getLength(), errormsg)
		);
	}

	@Test
	void PropertyParserStringTest() {
		String errormsg = "Property parser failed to correctly parse a property of type string";

		assertAll(
			() -> assertNull((new PropertyParser()).parse("hello"), errormsg),
			() -> assertNull((new PropertyParser()).parse(" hello:\"test\""), errormsg),
			() -> assertNull((new PropertyParser()).parse(""), errormsg),
			() -> assertNull((new PropertyParser()).parse("test:"), errormsg),
			() -> assertNull((new PropertyParser()).parse("test: "), errormsg)
		);

		ParserProperty property1 = (new PropertyParser()).parse("test:\"tester\"");
		ParserProperty property2 = (new PropertyParser()).parse("test:'tester'");
		assertAll(
			() -> assertEquals("test", property1.getName(), errormsg),
			() -> assertEquals("tester", ((ParserString) property1.getContent()).getString(), errormsg),
			() -> assertEquals("tester", ((ParserString) property2.getContent()).getString(), errormsg)
		);
	}
}