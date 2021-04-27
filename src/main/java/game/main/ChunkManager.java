package game.main;

import game.entities.Chunk;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.ArrayList;
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
    private ArrayList<Chunk> activeChunks;

    private ChunkManager() {}
    public ChunkManager(X x) {
        this.x = x;
        activeChunks = new ArrayList<Chunk>();
        chunks = new ArrayList<ArrayList<Chunk>>();
    }

    public void loadChunks(Path path) {
        // TODO send each path to parser API and create new chunks by passing parser blocks into Chunk.load(...)
        Pattern pattern = Pattern.compile("[0-9]{6}\\.msv");
        try ( Stream<Path> paths = Files.walk(path.resolve(Paths.get("chunks"))) ) {
            paths.forEach(f -> {
                Matcher matcher = pattern.matcher(f.toString());
                if ( matcher.find() ) {
                    // TODO process each path
                }
            });
        } catch ( IOException e ) {System.err.println("Failed to read chunk files\n" + e);}
    }
}
