package util.parse;

import util.parse.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlockParserTests {
	@Test
	void InvalidInputTest() {
		String errormsg = "BlockParser failed to return null when there was no whitespace to parse";
    String errormsg2 = "BlockParser failed to count number of characters parsed";
    BlockParser blockParser = new BlockParser();		

    assertNull(blockParser.parse("{"), errormsg);
    assertEquals(0, blockParser.getParsedLength(), errormsg2);
	  assertNull(blockParser.parse("test"), errormsg);
    assertEquals(0, blockParser.getParsedLength(), errormsg2);
	  assertNull(blockParser.parse("{test}"), errormsg);
    assertEquals(0, blockParser.getParsedLength(), errormsg2);
	}

  @Test
  void StringPropertyBlockTest() {
    String errormsg = "BlockParser failed to correctly parse a block consisting of only string properties";
    String errormsg2 = "BlockParser failed to correctly count number of characters parsed in a block with only string properties";
    BlockParser parser = new BlockParser();
    
    ParserBlock block = (ParserBlock) parser.parse("{first: 'test', second: 'test2'}");
    assertEquals(32, parser.getParsedLength(), errormsg2);
    assertEquals("test", ((ParserString) (block.getProperties().get("first"))).getString(), errormsg);
    assertEquals("test2", ((ParserString) (block.getProperties().get("second"))).getString(), errormsg);
    
    block = (ParserBlock) parser.parse("{first: 'test'}");
    assertEquals(15, parser.getParsedLength(), errormsg2);
    assertEquals("test", ((ParserString) (block.getProperties().get("first"))).getString(), errormsg);
  }
  
  @Test
  void ComplexPropertyTest() {
    String errormsg = "BlockParser failed to correctly parse a block consisting of complex mixed properties";
    String errormsg2 = "BlockParser failed to correctly count number of characters parsed in a block with complex mixed properties properties";
    BlockParser parser = new BlockParser();
    
    ParserBlock block = (ParserBlock) parser.parse("{first: ['test', 'tst'], second: 'test2', third: {inner: 'inside'}}");
    assertEquals(67, parser.getParsedLength(), errormsg2);
    assertEquals("test2", ((ParserString) (block.getProperties().get("second"))).getString(), errormsg);
    assertEquals("tst", ((ParserString) (((ParserArray) (block.getProperties().get("first"))).getIndex(1))).getString(), errormsg);
    assertEquals("inside", ((ParserString) (((ParserBlock) (block.getProperties().get("third"))).getProperties().get("inner"))).getString(), errormsg);
  }
    @Test 
    void ToJSONTest() {
        String errormsg = "Block object returned by parser failed to correctly output equivalent JSON";

        ParserBlock block1 = (new BlockParser()).parse("{ i: 2, str: \"hello\", blk: { j: 3 } }");
        ParserBlock block2 = (new BlockParser()).parse(block1.toString());

        assertEquals(2, ((ParserInt)block2.getProperty("i")).getNumber(), errormsg);
        assertEquals("hello", ((ParserString)block2.getProperty("str")).getString(), errormsg);
        assertEquals(3, ((ParserInt) ((ParserBlock)block2.getProperty("blk")).getProperty("j")).getNumber(), errormsg);
    }
}
