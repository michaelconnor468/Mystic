package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class NumberParserTests {
	@Test
	void InvalidArgumentsTest() {
		String errormsg = "Number parser failed to correctly return null when given a non-number";

		assertAll(
			() -> assertNull((new NumberParser()).parse(""), errormsg),
			() -> assertNull((new NumberParser()).parse("test"), errormsg),
			() -> assertNull((new NumberParser()).parse("t123"), errormsg),
			() -> assertNull((new NumberParser()).parse(".4325"), errormsg)
		);
	}

	@Test
	void IntegerParsingTest() {
		String errormsg2 = "Number parser failed to correctly parse a integer";

		assertAll(
			() -> assertEquals(123, ((ParserInt) (new NumberParser()).parse("123")).getNumber(), errormsg2),
			() -> assertEquals(0, ((ParserInt) (new NumberParser()).parse("0")).getNumber(), errormsg2),
			() -> assertEquals(123, ((ParserInt) (new NumberParser()).parse("123 ")).getNumber(), errormsg2),
			() -> assertEquals(0, ((ParserInt) (new NumberParser()).parse("0,")).getNumber(), errormsg2)
		);
	}

	@Test
	void DoubleParsingTest() {
		String errormsg3 = "Number parser failed to correctly parse a floating point";

		assertAll(
			() -> assertEquals(0.123, ((ParserDouble) (new NumberParser()).parse("0.123")).getNumber(), errormsg3),
			() -> assertEquals(14.24, ((ParserDouble) (new NumberParser()).parse("14.24")).getNumber(), errormsg3),
			() -> assertEquals(0.123, ((ParserDouble) (new NumberParser()).parse("0.123 ")).getNumber(), errormsg3),
			() -> assertEquals(14.24, ((ParserDouble) (new NumberParser()).parse("14.24,")).getNumber(), errormsg3)
		);
	}

    @Test
    void ParseCountTest() {
        String errormsg = "Number parser failed to correctly count number of characters parsed";

        NumberParser parser = new NumberParser();
        parser.parse("");
        assertEquals(0, parser.getParsedLength(), errormsg);
        parser.parse("123");
        assertEquals(3, parser.getParsedLength(), errormsg);
        parser.parse("0.32");
        assertEquals(4, parser.getParsedLength(), errormsg);
        parser.parse("123test");
        assertEquals(3, parser.getParsedLength(), errormsg);
    }

    @Test
    void OutputTest() {
        String errormsg = "Number parser objects failed to correctly stringify.";

        NumberParser parser = new NumberParser();
        assertEquals("0", ((ParserInt) parser.parse("0")).toString(), errormsg);
        assertEquals("102", ((ParserInt) parser.parse("102")).toString(), errormsg);
        assertEquals(0, ((ParserDouble) parser.parse(((ParserDouble) parser.parse("0.0")).toString())).getNumber());
    }
}
