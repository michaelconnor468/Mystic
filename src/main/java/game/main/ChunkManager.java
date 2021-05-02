package game.main;

import game.entities.Chunk;
import util.parse.FileParser;
import util.parse.obj.ParserBlock;
import util.parse.obj.ParserInt;

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
    private int chunkSize, tileSize, chunkGridSize, chunkLoadDiameter;

    private ChunkManager() {}

    public ChunkManager(X x, ParserBlock worldSettings) {
        this.x = x;
        this.chunkSize = ((ParserInt) worldSettings.getProperties().get("chunkSize")).getNumber();
        this.tileSize = ((ParserInt) worldSettings.getProperties().get("tileSize")).getNumber();
        this.chunkGridSize = ((ParserInt) worldSettings.getProperties().get("chunkGridSize")).getNumber();
        this.chunkLoadDiameter = ((ParserInt) x.getWorldSettings().getProperties().get("chunkLoadDiameter")).getNumber(); 
        this.activeChunks = new ArrayList<ArrayList<Chunk>>(Collections.nCopies(chunkLoadDiameter, 
            new ArrayList<Chunk>(Collections.nCopies(chunkLoadDiameter, null))));
        this.chunks = new ArrayList<ArrayList<Chunk>>(Collections.nCopies(chunkGridSize, 
            new ArrayList<Chunk>(Collections.nCopies(chunkGridSize, null))));
    }

    public void loadChunks(Path path) {
        Pattern pattern = Pattern.compile("[0-9]{6}\\.msv");
        try ( Stream<Path> paths = Files.walk(path.resolve(Paths.get("chunks"))) ) {
            paths.forEach(f -> {
                Matcher matcher = pattern.matcher(f.toString());
                if ( matcher.find() ) {
                    int column = Integer.parseInt(matcher.group().substring(0,3));
                    int row = Integer.parseInt(matcher.group().substring(3,6));
                    chunks.get(column).set(row, Chunk.load(x, FileParser.parse(f)));
                }
            });
        } catch ( Exception e ) { 
            System.err.println("Failed to read chunk files\n" + e);
            System.exit(1);
        }
    }

    public int getChunkSize() { return chunkSize; }
    public int getTileSize() { return tileSize; }
    public int getChunkGridSize() { return chunkGridSize; }
    public int getChunkLoadDiameter() { return chunkLoadDiameter; }
    public ArrayList<ArrayList<Chunk>> getChunks() { return chunks; }
    public ArrayList<ArrayList<Chunk>> getActiveChunks() { return activeChunks; }
}
