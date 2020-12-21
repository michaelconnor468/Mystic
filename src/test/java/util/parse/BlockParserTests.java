package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlockParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "BlockParser failed to return null when there was no whitespace to parse";

		assertNull((new BlockParser()).parse("{"), errormsg);
	  assertNull((new BlockParser()).parse("test"), errormsg);
	  assertNull((new BlockParser()).parse("{test}"), errormsg);
	}
}
