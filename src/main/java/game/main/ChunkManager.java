package game.main;

import game.entities.Chunk;
import util.parse.FileParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an interface for the collection of discrete abstract units known as chunks which hold all of the objects that funtion and
 * interact with each other within the game. Delegates ticks to the chunks so they may dispatch them to thir corresponding entities. 
 * Implements the part of the game's api which is used to manage entities within the game.
 */
public class ChunkManager {
    private X x;
    private ArrayList<ArrayList<Chunk>> chunks;
    private ArrayList<ArrayList<Chunk>> activeChunks;

    private ChunkManager() {}
    public ChunkManager(X x) {
        this.x = x;
        // TODO read numbers from config files
        activeChunks = new ArrayList<ArrayList<Chunk>>(Collections.nCopies(3, 
            new ArrayList<Chunk>(Collections.nCopies(3, null))));
        chunks = new ArrayList<ArrayList<Chunk>>(Collections.nCopies(100, 
            new ArrayList<Chunk>(Collections.nCopies(100, null))));
    }

    public void loadChunks(Path path) {
        Pattern pattern = Pattern.compile("[0-9]{6}\\.msv");
        try ( Stream<Path> paths = Files.walk(path.resolve(Paths.get("chunks"))) ) {
            paths.forEach(f -> {
                Matcher matcher = pattern.matcher(f.toString());
                if ( matcher.find() ) {
                    int column = Integer.parseInt(matcher.group().substring(0,3));
                    int row = Integer.parseInt(matcher.group().substring(3,6));
                    System.out.println(chunks.size());
                    // TODO debug null block
                    chunks.get(column).set(row, Chunk.load(x, FileParser.parse(f)));
                }
            });
        } catch ( IOException e ) {System.err.println("Failed to read chunk files\n" + e);}
    }
}
