package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ParserTests {


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