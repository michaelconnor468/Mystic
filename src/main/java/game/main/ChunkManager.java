package game.main;

import game.entities.Chunk;
import game.entities.Entity;
import game.entities.TileEntity;
import game.main.render.Renderable;
import game.main.render.Renderer;
import util.parse.FileParser;
import util.parse.obj.ParserBlock;
import util.parse.obj.ParserInt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an interface for the collection of discrete abstract units known as chunks which hold all of the objects that funtion and
 * interact with each other within the game. Delegates ticks to the chunks so they may dispatch them to thir corresponding entities. 
 * Implements the part of the game's api which is used to manage entities within the game.
 */
public class ChunkManager implements TickObserver, Renderable {
    private X x;
    private ArrayList<ArrayList<Chunk>> chunks;
    private int chunkSize, tileSize, chunkLoadDiameter;

    private ChunkManager() {}

    public ChunkManager(X x) {
        this.x = x;
        int j = 0;
        this.chunkSize = ((ParserInt) x.getMainSettings().get("chunkSize")).getNumber();
        this.tileSize = ((ParserInt) x.getMainSettings().get("tileSize")).getNumber();
        this.chunkLoadDiameter = ((ParserInt) x.getMainSettings().get("chunkLoadDiameter")).getNumber(); 
        this.chunks = new ArrayList<ArrayList<Chunk>>();
        for ( int i = 0; i < this.chunkLoadDiameter; i++ )
            this.chunks.add(new ArrayList<Chunk>(Collections.nCopies(chunkLoadDiameter, null)));
        x.getTimingManager().register(this);
        x.getRenderManager().register(this);
    }

    public void tick(X x) {
        for ( ArrayList<Chunk> chunkList : chunks ) for ( Chunk chunk : chunkList ) chunk.tick(x);
    }

    public void render(Renderer r) {
        for ( ArrayList<Chunk> chunkList : chunks ) for ( Chunk chunk : chunkList ) chunk.render(r);
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
                    chunks.get(column).set(row, new Chunk(x, FileParser.parse(f), column, row));
                }
            });
        } catch ( Exception e ) { 
            e.printStackTrace(new java.io.PrintStream(System.err));  
            System.exit(1);
        }
    }

    public boolean testCollision( Entity entity ) {
        for ( Chunk chunk : getChunksAround(entity) ) {
            if ( chunk.testCollision(entity) )
                return true;
        }
        return false;
    }

    public Chunk getChunkInsideOf( Entity entity ) {
        int chunkSizePX = chunkSize*tileSize;
        return chunks.get((int) entity.getPosition().getX()/chunkSizePX).get((int) entity.getPosition().getY()/chunkSizePX);
    }

    public void addEntity( Entity entity ) { getChunkInsideOf(entity).addEntity(entity); }
    public void removeEntity( Entity entity ) { getChunkInsideOf(entity).removeEntity(entity); }

    public ArrayList<Chunk> getChunksAround( Entity entity ) {
        ArrayList<Chunk> ret = new ArrayList<>();
        int middlexChunk = (int) entity.getPosition().getX()/(chunkSize*tileSize);
        int middleyChunk = (int) entity.getPosition().getY()/(chunkSize*tileSize);
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

    public ArrayList<TileEntity> getTilesAround( Entity entity ) {
        ArrayList<TileEntity> ret = new ArrayList<>();
        for ( Chunk chunk : getChunksAround(entity) ) 
            for ( TileEntity tileEntity : chunk.getTilesAround(entity) )
                ret.add(tileEntity);
        return ret;
    }

    public List<Chunk> getChunks() { 
        ArrayList<Chunk> ret = new ArrayList<>();
        for ( ArrayList<Chunk> lst : chunks ) for ( Chunk chunk : lst ) ret.add(chunk);
        return ret;
    }

    public void saveChunks(Path path) { for ( Chunk chunk : getChunks() ) saveChunk(path, chunk); }
    public void saveChunk(Path path, Chunk chunk) {
        try {
            Files.write(path.resolve(Paths.get("chunk"+chunk.getXChunkPosition()+""+chunk.getYChunkPosition()+".msv")),
                chunk.save(new ParserBlock()).toString().getBytes());
        } catch (Exception e) { System.err.println("Unable to save chunk."); }
    }
    
    public int getChunkSize() { return chunkSize; }
    public int getTileSize() { return tileSize; }
    public int getChunkLoadDiameter() { return chunkLoadDiameter; }
    public ArrayList<ArrayList<Chunk>> getActiveChunks() { return chunks; }
}
