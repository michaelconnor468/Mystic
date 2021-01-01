package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SequenceParserTests {
	@Test
	void SequenceParserTest() {
		SequenceParser parser = new SequenceParser("hello");
		String errormsg = "SequenceParser failed to return null when a string did not match the parsed text";

		assertAll(
			() -> assertNull(parser.parse(" hello"), errormsg),
			() -> assertNull(parser.parse("he llo"), errormsg),
			() -> assertNull(parser.parse("he\nllo"), errormsg),
			() -> assertNull(parser.parse("_hello"), errormsg),
			() -> assertNull(parser.parse("abc"), errormsg),
			() -> assertNull(parser.parse("\nhello"), errormsg),
			() -> assertNull(parser.parse(""), errormsg)
		);
	}

	@Test
	void MatchingSequenceTest() {
		SequenceParser parser = new SequenceParser("hello");
		String errormsg = "SequenceParser failed to correctly match a substring at the start of a string";

		assertAll(
			() -> assertEquals("hello", parser.parse("hello"), errormsg),
			() -> assertEquals("hello", parser.parse("hellohi"), errormsg),
			() -> assertEquals("hello", parser.parse("hello hi"), errormsg),
			() -> assertEquals("hello", parser.parse("hello\nhi"), errormsg)
		);
	}

  @Test
  void ParserCountTest() {
    SequenceParser parser = new SequenceParser("test");
    String errormsg = "SequenceParser failed to correctly count number of characters parsed";

    parser.parse("test");
    assertEquals(4, parser.getParsedLength(), errormsg);
    parser.parse("test test");
    assertEquals(4, parser.getParsedLength(), errormsg);
    parser.parse("ntest");
    assertEquals(0, parser.getParsedLength(), errormsg);
  }
}
