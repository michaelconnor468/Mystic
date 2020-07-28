package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CaseParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "Case parser failed to return null on invalid input";

		assertAll(
			() -> assertNull((new CaseParser(ParserObject.ObjectType.STRING)).parse("['hello']"), errormsg),
			() -> assertNull((new CaseParser(ParserObject.ObjectType.INT)).parse("'hello'"), errormsg),
//	TODO		() -> assertNull((new CaseParser(ParserObject.ObjectType.ARRAY)).parse("'hello'"), errormsg),
//	TODO		() -> assertNull((new CaseParser(ParserObject.ObjectType.BLOCK)).parse("'hello'"), errormsg),
			() -> assertNull((new CaseParser(ParserObject.ObjectType.DOUBLE)).parse("436"), errormsg),
			() -> assertNull((new CaseParser()).parse(" 'hello'"), errormsg)
		);
	}

	@Test
	void DiscernTypeTest() {
		String errormsg = "Case parser failed to discern correct type of object and parse it afterwards";

		assertAll(
			() -> assertEquals(645, ((ParserInt)(new CaseParser()).parse("645")).getNumber(), errormsg),
			() -> assertEquals(4.645, ((ParserDouble)(new CaseParser()).parse("4.645")).getNumber(), errormsg),
// TODO add case for array
// TODO add case for block
			() -> assertEquals("hello", (new CaseParser()).parse("'hello'"), errormsg)
		);
	}
}
