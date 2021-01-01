package util.parse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BracketedExpressionParserTests {
	@Test
	void InvalidBracketInputTest() {
		String errormsg = "Bracketed expression parser failed to return null when invalid bracket input was supplied.";

		assertAll(
			() -> assertNull((new BracketedExpressionParser()).parse(" [ test ]"), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse(""), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse("[ hi "), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse("{"), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse("{ hello { hi }"), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse("{ \n\t { \n }"), errormsg)
		);
	}

	@Test
	void InvalidQuoteInputTest() {
		String errormsg = "Bracketed expression parser failed to return null when invalid quote input was supplied.";

		assertAll(
			() -> assertNull((new BracketedExpressionParser()).parse("\""), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse("\" \n"), errormsg),
			() -> assertNull((new BracketedExpressionParser()).parse("'hello"), errormsg)
		);
	}

	@Test
	void BracketExpressionTest() {
		String errormsg = "Bracketed expression parser failed to parse bracketed input.";

		assertAll(
			() -> assertEquals("{test}", (new BracketedExpressionParser()).parse("{test}"), errormsg),
			() -> assertEquals("{\n{test} test}", (new BracketedExpressionParser()).parse("{\n{test} test}"), errormsg),
			() -> assertEquals("[test]", (new BracketedExpressionParser()).parse("[test]"), errormsg),
			() -> assertEquals("(test)", (new BracketedExpressionParser()).parse("(test)"), errormsg)
		);
	}

	@Test
	void QuotedExpressionTest() {
		String errormsg = "Bracketed expression parser failed to parse quoted input.";

		assertAll(
			() -> assertEquals("\"test\"", (new BracketedExpressionParser()).parse("\"test\""), errormsg),
			() -> assertEquals("'test'", (new BracketedExpressionParser()).parse("'test'"), errormsg)
		);
	}

  @Test
  void ParsedCountTest() {
    String errormsg = "Bracketed expression parser failed to correctly count number of characters parsed.";

    BracketedExpressionParser parser = new BracketedExpressionParser();
    parser.parse("[test]");
    assertEquals(6, parser.getParsedLength(), errormsg);
    parser.parse("");
    assertEquals(0, parser.getParsedLength(), errormsg);
    parser.parse("[test");
    assertEquals(0, parser.getParsedLength(), errormsg);
    parser.parse("{\n{test} test}");
    assertEquals(14, parser.getParsedLength(), errormsg);
  }
}
