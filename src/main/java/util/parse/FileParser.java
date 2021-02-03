package util.parse;

import util.parse.obj.ParserBlock;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility used to extract or write a desired parserblock from or to a file
 */
public class FileParser {
    public static ParserBlock parse(Path path) {
        ParserBlock block = null;
        try {
            block = (new BlockParser()).parse(Files.readString(path));
        }
        catch(java.io.IOException e) {
            System.err.print("Failed to read ParserBlock from file path: ");
            System.err.print(path.toString());
            System.err.print("\n");
        }
        return block; 
    }

    public static void write(Path path, ParserBlock block) {
        try {
            Files.writeString(path, block.toJSON());
        }
        catch(java.io.IOException e) {
            System.err.print("Failed to write ParserBlock to file path: ");
            System.err.print(path.toString());
            System.err.print("\n");
        }
    }
}
