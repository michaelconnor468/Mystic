# JSON Parser Library

## Overview
The util.parse library is intended to parse files in block json format using the API's BlockParser. Once a section of JSON is parsed, it is represented by one of a few parser objects in util.parse.obj which store the data extracted from the JSON in a natural way. These can then be used by the game to load static settings and save files for the game in order to setup its state on startup in a way that is easy to change and modify.

&nbsp;
&nbsp;

## Parser Logic
The parser is designed in the object oriented style. Each parsing object represents an element of the grammar or a helper object. All parser objects are based off of the Parser interface which specifies a single method 'parse()'. This takes in the string to parse as the input and only parses as far into the string as is defined by the grammar. If the string does not match the grammar it is coded for, then null is returned from the method. Otherwise, a util.parse.obj object is returned representing the data that was parsed.

### Helper Parsers
Some helper objects are used to help the grammar objects parse more effectively and are only used internally by the main parsers and not the parser interface. These include parsing operartions that are commonly performed and serve to isolate their logic. For instance suppose we want to parse a logical block of code between some natural characters or brackets; Bracketed Expression Parser does just this. Consider the example below,
```java
String block = 
"{
    block1 = {
        property = \" test \"
    }
}

property1 = {
    
}";

System.out.println((new BracketedExpressionParser()).parse(block));
```
This code returns the first block along with its enclosing characters. This example is a prime depiction of how these parser objects work, reading only the bit of the string they are designed to, and ignoring the rest.

```java
{
    block1 = {
        property = \" test \"
    }
}
```

### Main Parsers
These parsers parse through the meat of the grammar, extracrting useful information to be used by the rest of the program. The parser interface drives parsing of the file using these parser objects which can recursively parse the JSON file format. One main parser that is often always called first by the interface and calls all other main parser objects is the property parser, which returns a Parser Property object representing a JSON property,

```java
ParserProperty property = (new PropertyParser()).parse("property1: \"test\"");
```

property is now a ParserProperty Object with the attributes: 
l. name: "property 1" 
l. propertyType: STRING
l. contents: "test"
