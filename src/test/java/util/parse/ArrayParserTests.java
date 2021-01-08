package util.parse;

import org.junit.jupiter.api.Test;
import util.parse.obj.ParserArray;
import util.parse.obj.ParserString;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "Failed to return null upon invalid input to ArrayParser.";

		assertNull((new ArrayParser()).parse("'test']"), errormsg);
		assertNull((new ArrayParser()).parse("['test'"), errormsg);
    assertNull((new ArrayParser()).parse(""), errormsg);
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
		String errormsg = "Array parser failed to correctly parse a nested array";

		assertEquals("test3",
				((ParserString)
					((ParserArray)
							(new ArrayParser()).parse("[['test1', 'test3'], ['test2']]").getIndex(0))
					.getIndex(1)
				).getString(), errormsg);
		assertEquals("test3",
				((ParserString)
					((ParserArray)
						((ParserArray)
							(new ArrayParser()).parse("[[['test1'], ['test3']], ['test2']]").getIndex(0)
						).getIndex(1)
					).getIndex(0)
				).getString(), errormsg);
		assertEquals("test2",
			((ParserString)
          		((ParserArray)
            		(new ArrayParser()).parse("[['test1', 'test3'], ['test2']]").getIndex(1)
          		).getIndex(0)
        	).getString(), errormsg);
	}

  @Test
  void ParsedLengthTest() {
    String errormsg = "Array parser failed to correctly count number of characters parsed";
    ArrayParser arrayParser = new ArrayParser();
    
    arrayParser.parse("'test']");
    assertEquals(0, arrayParser.getParsedLength(), errormsg);
    arrayParser.parse("['test'");
    assertEquals(0, arrayParser.getParsedLength(), errormsg);
    arrayParser.parse("");
    assertEquals(0, arrayParser.getParsedLength(), errormsg);

    arrayParser.parse("[['test1', 'test3'], ['test2']]");
    assertEquals(31, arrayParser.getParsedLength(), errormsg);
    arrayParser.parse("[]");
    assertEquals(2, arrayParser.getParsedLength(), errormsg);
  }
}
