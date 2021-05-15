package game.main;

import game.entities.Chunk;
import game.entities.Entity;
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
    private int chunkSize, tileSize, chunkGridSize, chunkLoadDiameter;

    private ChunkManager() {}

    public ChunkManager(X x, ParserBlock worldSettings) {
        this.x = x;
        this.chunkSize = ((ParserInt) worldSettings.getProperties().get("chunkSize")).getNumber();
        this.tileSize = ((ParserInt) worldSettings.getProperties().get("tileSize")).getNumber();
        this.chunkGridSize = ((ParserInt) worldSettings.getProperties().get("chunkGridSize")).getNumber();
        this.chunkLoadDiameter = ((ParserInt) x.getMainSettings().getProperties().get("chunkLoadDiameter")).getNumber(); 
        this.chunks = new ArrayList<ArrayList<Chunk>>();
        for ( int i = 0; i < this.chunkGridSize; i++ )
            this.chunks.add(new ArrayList<Chunk>(Collections.nCopies(chunkGridSize, null)));
    }

    public void loadChunks(Path path) {
        Pattern pattern = Pattern.compile("[0-9]{6}\\.msv");
        try ( Stream<Path> paths = Files.walk(path.resolve(Paths.get("chunks"))) ) {
            paths.forEach(f -> {
                Matcher matcher = pattern.matcher(f.toString());
                if ( matcher.find() ) {
                    assert matcher.group().length() == 10;
                    int column = Integer.parseInt(matcher.group().substring(0,3));
                    int row = Integer.parseInt(matcher.group().substring(3,6));
                    chunks.get(column).set(row, Chunk.load(x, FileParser.parse(f), column, row));
                }
            });
        } catch ( Exception e ) { 
            System.err.println("Failed to read chunk files\n" + e);
            System.exit(1);
        }
    }

    public boolean isColliding( Entity entity ) {
        for ( Chunk chunk : getChunksAround(entity) ) {
            if ( chunk.isColliding(entity) )
                return true;
        }
        return false;
    }

    public Chunk getChunkInsideOf( Entity entity ) {
        int chunkSizePX = chunkSize*tileSize;
        return chunks.get((int) entity.getxPosition()/chunkSizePX).get((int) entity.getyPosition()/chunkSizePX);
    }
    public ArrayList<Chunk> getChunksAround( Entity entity ) {
        ArrayList<Chunk> ret = new ArrayList<>();
        int middlexChunk = (int) entity.getxPosition()/(chunkSize*tileSize);
        int middleyChunk = (int) entity.getyPosition()/(chunkSize*tileSize);
        for ( int i = -1; i < 2; i++ ) {
            for ( int j = -1; j < 2; j++ ) {
                if ( middlexChunk + i >= 0 && middlexChunk + i < chunks.size() ) {
                    if ( middleyChunk + j >= 0 && middleyChunk + j < chunks.get(middlexChunk + i).size() )
                        ret.add(chunks.get(middlexChunk + i).get(middleyChunk + j));
                }
            }
        }
        return ret;
    }
    
    public int getChunkSize() { return chunkSize; }
    public int getTileSize() { return tileSize; }
    public int getChunkGridSize() { return chunkGridSize; }
    public int getChunkLoadDiameter() { return chunkLoadDiameter; }
    public ArrayList<ArrayList<Chunk>> getChunks() { return chunks; }
    public ArrayList<ArrayList<Chunk>> getActiveChunks() { return chunks; }
}
