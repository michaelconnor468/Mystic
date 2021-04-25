package game.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Represents an interface for the collection of discrete abstract units known as chunks which hold all of the objects that funtion and
 * interact with each other within the game. Delegates ticks to the chunks so they may dispatch them to thir corresponding entities. 
 * Implements the part of the game's api which is used to manage entities within the game.
 */
public class ChunkManager {
    public ChunkManager() {

    }

    public void loadGame(Path path) {
        // TODO send each path to parser API and create new chunks by passing parser blocks into Chunk.load(...)
        try ( Stream<Path> paths = Files.walk(path.resolve(Paths.get("chunks"))) ) {
            paths.forEach((f) -> {
                return;
            });
        } catch ( Exception e ) { System.err.println("Failed to get chunk paths: " + e); System.exit(1); }
    }
}
