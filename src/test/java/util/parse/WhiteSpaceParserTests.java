package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class WhiteSpaceParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "WhitespaceParser failed to return null when there was no whitespace to parse";

		assertAll(
			() -> assertNull((new WhitespaceParser()).parse(""), errormsg),
			() -> assertNull((new WhitespaceParser()).parse("test"), errormsg),
			() -> assertNull((new WhitespaceParser()).parse("_test"), errormsg)
		);
	}

	@Test
	void SpaceCharacterTest() {
		String errormsg = "WhitespaceParser failed to parse space characters at the start of a string";

		assertAll(
			() -> assertEquals(" ", (new WhitespaceParser()).parse(" "), errormsg),
			() -> assertEquals(" ", (new WhitespaceParser()).parse(" test"), errormsg),
			() -> assertEquals("   ", (new WhitespaceParser()).parse("   test"), errormsg)
		);
	}

	@Test
	void SpecialWhitespaceCharacterTest() {
		String errormsg = "WhitespaceParser failed to parse special characters such as new lines, tabs, and returns";

		assertAll(
			() -> assertEquals("\n", (new WhitespaceParser()).parse("\ntest"), errormsg),
			() -> assertEquals("\t", (new WhitespaceParser()).parse("\ttest"), errormsg),
			() -> assertEquals("\r", (new WhitespaceParser()).parse("\rtest"), errormsg),
			() -> assertEquals("  \n  \t ", (new WhitespaceParser()).parse("  \n  \t test"), errormsg)
		);
	}
}
