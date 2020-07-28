package util.parse;

import org.junit.jupiter.api.Test;
import util.parse.obj.ParserString;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "Failed to return null upon invalid input to ArrayParser.";

		assertAll(
			() -> assertNull((new PropertyParser()).parse("'test']"), errormsg),
			() -> assertNull((new PropertyParser()).parse("['test'"), errormsg),
		    () -> assertNull((new PropertyParser()).parse(""), errormsg)
		);
	}

	@Test
	void NonNesterArrayTest() {
		String errormsg = "ArrayParser failed to parse non-nested array.";
		assertAll(
			() -> assertEquals("test1", ((ParserString) (new ArrayParser()).parse("['test1', 'test2']").getIndex(0)).getString(), errormsg),
			() -> assertEquals("test2", ((ParserString) (new ArrayParser()).parse("['test1', 'test2']").getIndex(1)).getString(), errormsg),
			() -> assertEquals(2, (new ArrayParser()).parse("['test1', 'test2']").getLength(), errormsg),
			() -> assertEquals(0, (new ArrayParser()).parse("[]").getLength(), errormsg)
		);
	}

	@Test
	void NestedArrayTest() {

	}
}
