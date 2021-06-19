package util.parse;

import util.parse.obj.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CaseParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "Case parser failed to return null on invalid input";

		assertNull((new CaseParser(ParserObject.ObjectType.INT)).parse("'hello'"), errormsg);
		assertNull((new CaseParser(ParserObject.ObjectType.STRING)).parse("['hello']"), errormsg);
		assertNull((new CaseParser(ParserObject.ObjectType.ARRAY)).parse("'hello'"), errormsg);
		assertNull((new CaseParser(ParserObject.ObjectType.BLOCK)).parse("'hello'"), errormsg);
		assertNull((new CaseParser(ParserObject.ObjectType.DOUBLE)).parse("436"), errormsg);
		assertNull((new CaseParser()).parse(" 'hello'"), errormsg);
	}

	@Test
	void DiscernTypeTest() {
		String errormsg = "Case parser failed to discern correct type of object and parse it afterwards";

		assertEquals(645, ((ParserInt)(new CaseParser()).parse("645")).getNumber(), errormsg);
		assertEquals(4.645, ((ParserDouble)(new CaseParser()).parse("4.645")).getNumber(), errormsg);
		assertEquals("test", ((ParserString) ((ParserArray)(new CaseParser()).parse("['test', 'test2']")).getIndex(0)).getString(), errormsg);
    assertEquals("test", ((ParserString) ((ParserBlock) (new CaseParser()).parse("{first: 'test', second: 'test2'}")).getProperty("first")).getString(), errormsg);
    assertEquals("hello", ((ParserString)(new CaseParser()).parse("'hello'")).getString(), errormsg);
	}

  @Test
  void ParsedLengthTest() {
    String errormsg = "Case Parser failed to correctly count the amount of characters parsed";
    
    CaseParser caseParser = new CaseParser();
    caseParser.parse("645");
    assertEquals(3, caseParser.getParsedLength());

    caseParser = new CaseParser();
    caseParser.parse("4.645");
    assertEquals(5, caseParser.getParsedLength());
    
    caseParser = new CaseParser();
    caseParser.parse("'test'");
    assertEquals(6, caseParser.getParsedLength());
  }
}
