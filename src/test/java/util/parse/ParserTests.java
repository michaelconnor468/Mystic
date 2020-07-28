package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ParserTests {


	@Test
	void ArrayParserTest() {
		String errormsg = "Array parser failed to correctly parse an array";



		assertAll(

			() -> assertEquals("test1", ((ParserString) ((ParserArray) (new ArrayParser()).parse("[['test1', 'test3'], ['test2']]").getIndex(1)).getIndex(0)).getString(), errormsg)

		);
	}
}