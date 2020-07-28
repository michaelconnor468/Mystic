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
}