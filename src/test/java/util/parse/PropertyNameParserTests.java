package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PropertyNameParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "PropertyNameParser failed to return null when invalid input was given";

		assertAll(
			() -> assertNull((new PropertyNameParser()).parse(""), errormsg),
			() -> assertNull((new PropertyNameParser()).parse(" abc"), errormsg),
			() -> assertNull((new PropertyNameParser()).parse("\nabc def"), errormsg),
			() -> assertNull((new PropertyNameParser()).parse("5abc def"), errormsg)
		);
	}

	@Test
	void StandardNameTest() {
		String errormsg = "PropertyNameParser failed to parse a name consisting of only characters";

		assertAll(
			() -> assertEquals("abc", (new PropertyNameParser()).parse("abc _def"), errormsg),
			() -> assertEquals("$Abc", (new PropertyNameParser()).parse("$Abc"), errormsg),
			() -> assertEquals("abc", (new PropertyNameParser()).parse("abc"), errormsg),
			() -> assertEquals("abc", (new PropertyNameParser()).parse("abc def"), errormsg)
		);
	}

	@Test
	void SpecialNameParserTest() {
		String errormsg = "PropertyNameParser incorrectly parsed a property name with characters other than letters";

		assertAll(

			() -> assertEquals("_abc", (new PropertyNameParser()).parse("_abc"), errormsg),
			() -> assertEquals("abc42", (new PropertyNameParser()).parse("abc42"), errormsg),
			() -> assertEquals("$", (new PropertyNameParser()).parse("$ abc"), errormsg),
			() -> assertEquals("ab_c", (new PropertyNameParser()).parse("ab_c"), errormsg)
		);
	}
}
