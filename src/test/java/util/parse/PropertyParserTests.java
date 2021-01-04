package util.parse;

import org.junit.jupiter.api.Test;
import util.parse.obj.ParserDouble;
import util.parse.obj.ParserInt;
import util.parse.obj.ParserProperty;
import util.parse.obj.ParserString;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "Property parser failed to correctly return null on invalid input.";

		assertAll(
			() -> assertNull((new PropertyParser()).parse("hello"), errormsg),
			() -> assertNull((new PropertyParser()).parse(" hello:\"test\""), errormsg),
			() -> assertNull((new PropertyParser()).parse(""), errormsg),
			() -> assertNull((new PropertyParser()).parse("test:"), errormsg),
			() -> assertNull((new PropertyParser()).parse("test: "), errormsg)
		);
	}

	@Test
	void StringPropertyTest() {
		String errormsg = "PropertyParser failed to correctly parse a string property";

		ParserProperty property1 = (new PropertyParser()).parse("test:\"tester\"");
		ParserProperty property2 = (new PropertyParser()).parse("test: 'tester'");
		assertAll(
			() -> assertEquals("test", property1.getName(), errormsg),
			() -> assertEquals("tester", ((ParserString) property1.getContent()).getString(), errormsg),
			() -> assertEquals("tester", ((ParserString) property2.getContent()).getString(), errormsg)
		);
	}

	@Test
	void NumberPropertyTest() {
		String errormsg = "PropertyParser failed to correctly parse a number property";

		ParserProperty property1 = (new PropertyParser()).parse("test: 546");
		ParserProperty property2 = (new PropertyParser()).parse("test: 23.467");
		assertAll(
			() -> assertEquals("test", property1.getName(), errormsg),
			() -> assertEquals(546, ((ParserInt) property1.getContent()).getNumber(), errormsg),
			() -> assertEquals(23.467, ((ParserDouble) property2.getContent()).getNumber(), errormsg)
		);
	}

  @Test
  void ParserLengthTest() {
    String errormsg = "PropertyParser failed to correctly count the total number of characters parsed";
    PropertyParser parser = new PropertyParser();

    parser.parse("test: 546");
    assertEquals(9, parser.getParsedLength(), errormsg);
    parser.parse("test:");
    assertEquals(0, parser.getParsedLength(), errormsg);
    parser.parse("");
    assertEquals(0, parser.getParsedLength(), errormsg);
    parser.parse("test:\"tester\"");
    assertEquals(13, parser.getParsedLength(), errormsg);
  }
}
