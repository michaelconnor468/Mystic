package util.parse;

import util.parse.obj.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ParserTests {
    @Test
    void WhitespaceParserTest() {
        WhitespaceParser whitespaceParser = new WhitespaceParser();
        String errormsg = "WhitespaceParser incorrectly parsed whitespace at start of string";

        assertAll(
            () -> assertNull(whitespaceParser.parse(""), errormsg),
            () -> assertNull(whitespaceParser.parse("test"), errormsg),
            () -> assertNull(whitespaceParser.parse("_test"), errormsg),

            () -> assertEquals(" " , whitespaceParser.parse(" "), errormsg),
            () -> assertEquals(" " , whitespaceParser.parse(" test"), errormsg),
            () -> assertEquals("   " , whitespaceParser.parse("   test"), errormsg),
            () -> assertEquals("\n" , whitespaceParser.parse("\ntest"), errormsg),
            () -> assertEquals("\t" , whitespaceParser.parse("\ttest"), errormsg),
            () -> assertEquals("\r" , whitespaceParser.parse("\rtest"), errormsg),
            () -> assertEquals("  \n  \t " , whitespaceParser.parse("  \n  \t test"), errormsg)
        );
    }

    @Test 
    void PropertyNameParserTest() {
        PropertyNameParser parser = new PropertyNameParser();
        String errormsg = "PropertyNameParser incorrectly parsed a property name at start of string";

        assertAll(
            () -> assertNull(parser.parse(""), errormsg),
            () -> assertNull(parser.parse(" abc"), errormsg),
            () -> assertNull(parser.parse("\nabc def"), errormsg),

            () -> assertEquals("abc" , parser.parse("abc"), errormsg),
            () -> assertEquals("_abc" , parser.parse("_abc"), errormsg),
            () -> assertEquals("$abc" , parser.parse("$abc"), errormsg),
            () -> assertEquals("$" , parser.parse("$ abc"), errormsg),
            () -> assertEquals("ab_c" , parser.parse("ab_c"), errormsg),
            () -> assertEquals("abc" , parser.parse("abc _def"), errormsg),
            () -> assertEquals("abc" , parser.parse("abc def"), errormsg)
        );
    }

    @Test
    void SequenceParserTest() {
        SequenceParser parser = new SequenceParser("hello");
        String errormsg = "SequenceParser incorrectly parsed a substring at start of string";

        assertAll(
            () -> assertNull(parser.parse(" hello"), errormsg),
            () -> assertNull(parser.parse("he llo"), errormsg),
            () -> assertNull(parser.parse("he\nllo"), errormsg),
            () -> assertNull(parser.parse("_hello"), errormsg),
            () -> assertNull(parser.parse("abc"), errormsg),
            () -> assertNull(parser.parse("\nhello"), errormsg),
            () -> assertNull(parser.parse(""), errormsg),

            () -> assertEquals("hello", parser.parse("hello"), errormsg),
            () -> assertEquals("hello", parser.parse("hellohi"), errormsg),
            () -> assertEquals("hello", parser.parse("hello hi"), errormsg),
            () -> assertEquals("hello", parser.parse("hello\nhi"), errormsg)
        );
    }

    @Test
    void BracketParserTest() {
        BracketedExpressionParser parser = new BracketedExpressionParser();
        String errormsg = "Bracketed expression parser failed to correctly parse a bracketed string correctly";

        assertAll(
            () -> assertNull(parser.parse("'hello"), errormsg),
            () -> assertNull(parser.parse(" [ test ]"), errormsg),
            () -> assertNull(parser.parse(""), errormsg),
            () -> assertNull(parser.parse("[ hi "), errormsg),
            () -> assertNull(parser.parse("{"), errormsg),
            () -> assertNull(parser.parse("{ hello { hi }"), errormsg),
            () -> assertNull(parser.parse("\""), errormsg),
            () -> assertNull(parser.parse("\" \n"), errormsg),
            () -> assertNull(parser.parse("{ \n\t { \n }"), errormsg),

            () -> assertEquals("{test}", parser.parse("{test}"), errormsg),
            () -> assertEquals("{\n{test} test}", parser.parse("{\n{test} test}"), errormsg),
            () -> assertEquals("[test]", parser.parse("[test]"), errormsg),
            () -> assertEquals("(test)", parser.parse("(test)"), errormsg),
            () -> assertEquals("\"test\"", parser.parse("\"test\""), errormsg),
            () -> assertEquals("'test'", parser.parse("'test'"), errormsg)
        );
    }

    @Test
    void NumberParserTest() {
        String errormsg = "Number parser failed to correctly parse a non-number";   
        
        assertAll(
            () -> assertNull((new NumberParser()).parse(""), errormsg),
            () -> assertNull((new NumberParser()).parse("test"), errormsg),
            () -> assertNull((new NumberParser()).parse("t123"), errormsg),
            () -> assertNull((new NumberParser()).parse(".4325"), errormsg)
        );

        String errormsg2 = "Number parser failed to correctly parse a integer";   

        assertAll(
            () -> assertEquals(123, ((ParserInt)(new NumberParser()).parse("123")).getNumber(), errormsg2),
            () -> assertEquals(0, ((ParserInt)(new NumberParser()).parse("0")).getNumber(), errormsg2),
            () -> assertEquals(123, ((ParserInt)(new NumberParser()).parse("123 ")).getNumber(), errormsg2),
            () -> assertEquals(0, ((ParserInt)(new NumberParser()).parse("0,")).getNumber(), errormsg2)
        );

        String errormsg3 = "Number parser failed to correctly parse a floating point";   

        assertAll(
            () -> assertEquals(0.123, ((ParserDouble)(new NumberParser()).parse("0.123")).getNumber(), errormsg3),
            () -> assertEquals(14.24, ((ParserDouble)(new NumberParser()).parse("14.24")).getNumber(), errormsg3),
            () -> assertEquals(0.123, ((ParserDouble)(new NumberParser()).parse("0.123 ")).getNumber(), errormsg3),
            () -> assertEquals(14.24, ((ParserDouble)(new NumberParser()).parse("14.24,")).getNumber(), errormsg3)
        );
    }

    @Test 
    void ArrayParserTest() {
        String errormsg = "Array parser failed to correctly parse an array";
        
        assertAll(
            () -> assertNull((new PropertyParser()).parse("'test']"), errormsg),
            () -> assertNull((new PropertyParser()).parse("['test'"), errormsg),
            () -> assertNull((new PropertyParser()).parse(""), errormsg)
        );

        /*assertAll(
            () -> assertEquals("test1", (new ArrayParser()).parse("['test1', 'test2']").getIndex(0), errormsg)
        );*/
    }

    @Test
    void PropertyParserStringTest() {
        String errormsg = "Property parser failed to correctly parse a property of type string";

        assertAll(
            () -> assertNull((new PropertyParser()).parse("hello"), errormsg),
            () -> assertNull((new PropertyParser()).parse(" hello:\"test\""), errormsg),
            () -> assertNull((new PropertyParser()).parse(""), errormsg),
            () -> assertNull((new PropertyParser()).parse("test:"), errormsg),
            () -> assertNull((new PropertyParser()).parse("test: "), errormsg)
        );

        Property property1 = (new PropertyParser()).parse("test:\"tester\"");
        Property property2 = (new PropertyParser()).parse("test:'tester'");
        assertAll(
            () -> assertEquals("test", property1.getName(), errormsg),
            () -> assertEquals("tester", ((ParserString)property1.getContent()).getString(), errormsg),
            () -> assertEquals("tester", ((ParserString)property2.getContent()).getString(), errormsg)
        );
    }
}