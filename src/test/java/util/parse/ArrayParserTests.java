package util.parse;

import org.junit.jupiter.api.Test;
import util.parse.obj.*;

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
		
        assertEquals("test1", ((ParserString) (new ArrayParser()).parse("['test1', 'test2']").getIndex(0)).getString(), errormsg);
        assertEquals("test2", ((ParserString) (new ArrayParser()).parse("['test1', 'test2']").getIndex(1)).getString(), errormsg);
		assertEquals(2, (new ArrayParser()).parse("['test1', 'test2']").getLength(), errormsg);
	    assertEquals(0, (new ArrayParser()).parse("[]").getLength(), errormsg);
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

    @Test
    void ToJSONTest() {
        String errormsg = "Array object returned by parser failed to correctly output equivalent JSON";

        ParserArray array = (new ArrayParser()).parse("[[6], [2,0], [1,4,3]]");
        ParserArray array2 = (new ArrayParser()).parse(array.toString());

        assertEquals(6, ((ParserInt) ((ParserArray) array2.getIndex(0)).getIndex(0)).getNumber(), errormsg);
        assertEquals(3, ((ParserInt) ((ParserArray) array2.getIndex(2)).getIndex(2)).getNumber(), errormsg);
        assertEquals(0, ((ParserInt) ((ParserArray) array2.getIndex(1)).getIndex(1)).getNumber(), errormsg);
        assertEquals(4, ((ParserInt) ((ParserArray) array2.getIndex(2)).getIndex(1)).getNumber(), errormsg);
    }
}
