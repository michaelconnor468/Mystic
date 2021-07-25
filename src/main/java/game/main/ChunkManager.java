package game.main;

import game.entities.Chunk;
import game.entities.Entity;
import game.entities.TileEntity;
import game.main.render.Renderable;
import game.main.render.Renderer;
import util.parse.BlockParser;
import util.parse.obj.ParserBlock;
import util.parse.obj.ParserInt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.concurrent.Task;
import java.awt.Point;

/**
 * Represents an interface for the collection of discrete abstract units known as chunks which hold all of the objects that funtion and
 * interact with each other within the game. Delegates ticks to the chunks so they may dispatch them to thir corresponding entities. 
 * Implements the part of the game's api which is used to manage entities within the game.
 */
public class ChunkManager implements TickObserver, Renderable {
    private X x;
    private ArrayList<ArrayList<Chunk>> chunks;
    private HashMap<String, String> chunkJSON = new HashMap<>();
    private Point center;
    private int chunkSize, tileSize, chunkLoadDiameter;
    private int maxX = 0;
    private int maxY = 0;

    private ChunkManager() {}

    public ChunkManager(X x) {
        this.x = x;
        int j = 0;
        this.chunkSize = ((ParserInt) x.getMainSettings().get("chunkSize")).getNumber();
        this.tileSize = ((ParserInt) x.getMainSettings().get("tileSize")).getNumber();
        this.chunkLoadDiameter = ((ParserInt) x.getMainSettings().get("chunkLoadDiameter")).getNumber(); 
        this.chunks = new ArrayList<ArrayList<Chunk>>();
        for ( int i = 0; i < this.chunkLoadDiameter*2 + 1; i++ )
            this.chunks.add(new ArrayList<Chunk>(Collections.nCopies(chunkLoadDiameter*2 + 1, null)));
        this.center = new Point(getCenterChunkX(), getCenterChunkY());
        x.getTimingManager().register(this);
        x.getRenderManager().register(this);
    }

    public void tick(X x) {
        for ( ArrayList<Chunk> chunkList : chunks ) for ( Chunk chunk : chunkList ) if (chunk != null) chunk.tick(x);
        refreshChunks();
    }

    public void render(Renderer r) {
        for ( ArrayList<Chunk> chunkList : chunks ) for ( Chunk chunk : chunkList ) if (chunk != null) chunk.render(r);
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
                    try {
                        chunkJSON.put(column+":"+row, Files.readString(f));
                    } catch ( IOException e ) { 
                        e.printStackTrace(new java.io.PrintStream(System.err));  
                        System.exit(1);
                    }
                }
            });
        } catch ( Exception e ) { 
            e.printStackTrace(new java.io.PrintStream(System.err));  
            System.exit(1);
        }
        for ( int i = 0; i < chunkLoadDiameter*2+1; i++) {
            for ( int j = 0; j < chunkLoadDiameter*2+1; j++) {
                int chunkRow =  i-chunkLoadDiameter+getCenterChunkX();
                int chunkColumn = j-chunkLoadDiameter+getCenterChunkY();
                ParserBlock block = (new BlockParser()).parse(chunkJSON.get(chunkRow+":"+chunkColumn));
                chunks.get(i).set(j, (new Chunk(x, block, chunkRow, chunkColumn)));
            }
        }
    }

    /**
     * Dynamically loads and saves chunks in and out of the active chunks to reduce the games resource footprint
     * and allow for near infinite maps.
     */
    private void refreshChunks() {
        if ( center.getX() != getCenterChunkX() ) {
            int columnOffset = getCenterChunkX() < center.getX() ? - chunkLoadDiameter : chunkLoadDiameter;
            ArrayList<Chunk> newList = new ArrayList<Chunk>(Collections.nCopies(chunkLoadDiameter*2+1, null));
            for ( int j = 0; j < chunkLoadDiameter*2 + 1; j++ ) {
                final int k = j;
                runLoadChunkTask(columnOffset, - chunkLoadDiameter + k, c -> newList.set(k, c));
                cacheChunk(chunks.get(getCenterChunkX() < center.getX() ? chunkLoadDiameter*2 : 0).get(j));
            }
            if ( getCenterChunkX() < center.getX() )
                for ( int i = chunkLoadDiameter*2; i > 0; i-- ) chunks.set(i, chunks.get(i-1));
            else
                for ( int i = 0; i < chunkLoadDiameter*2; i++ ) chunks.set(i, chunks.get(i+1));
            chunks.set(getCenterChunkX() < center.getX() ? 0 : chunkLoadDiameter*2, newList);
            center = new Point((int) getCenterChunkX(), (int) center.getY());
        }
        if ( center.getY() != getCenterChunkY() ) {
            int rowOffset = getCenterChunkY() < center.getY() ? -chunkLoadDiameter : chunkLoadDiameter;
            if ( getCenterChunkY() - center.getY() < 0 ) {
                for ( int i = 0; i < chunkLoadDiameter*2+1; i++ ) cacheChunk(chunks.get(i).get(chunkLoadDiameter*2));
                for ( int i = 0; i < chunkLoadDiameter*2 + 1; i++ )
                    for ( int j = chunkLoadDiameter*2; j > 0; j-- ) 
                        chunks.get(i).set(j, chunks.get(i).get(j-1));
                for ( int i = 0; i < chunkLoadDiameter*2 + 1; i++ ) {
                    final int k = i;
                    runLoadChunkTask(-chunkLoadDiameter+k, rowOffset, c -> chunks.get(k).set(0, c));
                }
            }
            else {
                for ( int i = 0; i < chunkLoadDiameter*2 + 1; i++ ) cacheChunk(chunks.get(chunkLoadDiameter*2).get(i));
                for ( int i = 0; i < chunkLoadDiameter*2 + 1; i++ )
                    for ( int j = 0 ; j < chunkLoadDiameter*2; j++ ) 
                        chunks.get(i).set(j, chunks.get(i).get(j+1));
                for ( int i = 0; i < chunkLoadDiameter*2 + 1; i++ ) {
                    final int k = i;
                    runLoadChunkTask(-chunkLoadDiameter+k, rowOffset, c -> chunks.get(k).set(chunkLoadDiameter*2, c));
                }
            }
            center = new Point((int) center.getX(), (int) getCenterChunkY());
        }
    }
   
    /**
     * Helper function for refreshing chunks.
     * Loads in a chunk in a separate thread with the given offset from the player and runs it through the given
     * consumer.
     */
    private void runLoadChunkTask(int colOffset, int rowOffset, java.util.function.Consumer<Chunk> onSuccess) {
        final Task<Object> task = new Task<Object>() {
            @Override protected Object call() throws Exception {
                int col = (getCenterChunkX()+colOffset);
                int row = (getCenterChunkY()+rowOffset);
                if ( chunkJSON.get(row+":"+col) == null ) return null;
                return new Chunk(x, (new BlockParser()).parse(chunkJSON.get(col+":"+row)), col, row);
            }
        };
        task.setOnSucceeded(v -> onSuccess.accept((Chunk) task.getValue()));
        (new Thread(task)).start();
    }

    /**
     *  Synchronized collision testing to allow for easy multithreading of arbitrary entities without rare
     *  race conditions related to moving entities popping up. Since collisions are only tested for nearby
     *  entities, the performance impact of synchrnization is negligible.
     */
    public synchronized boolean testCollision( Entity entity ) {
        for ( Chunk chunk : getChunksAround(entity) ) {
            if ( chunk == null ) continue;
            if ( chunk.testCollision(entity) )
                return true;
        }
        return false;
    }

    public Chunk getChunkInsideOf( Entity entity ) {
        return chunks.get((int) entity.getPosition().getX()/(chunkSize*tileSize) - getLeftmostChunk())
            .get((int) entity.getPosition().getY()/(chunkSize*tileSize) - getTopmostChunk());
    }

    private void printActiveChunks() {
        System.out.println("*** Chunks ***");
        for ( int i = 0; i < chunkLoadDiameter*2 + 1; i++ ) {
            System.out.print("[");
            for ( int k = 0; k < chunkLoadDiameter*2 + 1; k++ ) {
                if ( chunks.get(k) != null && chunks.get(k).get(i) != null )
                    System.out.print(chunks.get(k).get(i).getXChunkPosition() + ":" 
                        + chunks.get(k).get(i).getYChunkPosition() + ", ");
                else
                    System.out.print(" null,");
            }
            System.out.print("]\n");
        }
        System.out.println("*************");
    }

    private int getCenterChunkX() { return (int) x.getPlayer().getPosition().getX()/(chunkSize*tileSize); }
    private int getCenterChunkY() { return (int) x.getPlayer().getPosition().getY()/(chunkSize*tileSize); }
    private int getLeftmostChunk() { return getCenterChunkX() - chunkLoadDiameter; }
    private int getTopmostChunk() { return getCenterChunkY() - chunkLoadDiameter; }

    public void addEntity( Entity entity ) { getChunkInsideOf(entity).addEntity(entity); }
    public void removeEntity( Entity entity ) { getChunkInsideOf(entity).removeEntity(entity); }

    public ArrayList<Chunk> getChunksAround( Entity entity ) {
        ArrayList<Chunk> ret = new ArrayList<>();
        int middlexChunk = (int) entity.getPosition().getX()/(chunkSize*tileSize) - getLeftmostChunk();
        int middleyChunk = (int) entity.getPosition().getY()/(chunkSize*tileSize) - getTopmostChunk();
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
        for ( Chunk chunk : getChunksAround(entity) ) {
            if ( chunk == null ) continue;
            for ( TileEntity tileEntity : chunk.getTilesAround(entity) ) ret.add(tileEntity);
        }
        return ret;
    }

    public List<Chunk> getChunks() { 
        ArrayList<Chunk> ret = new ArrayList<>();
        for ( ArrayList<Chunk> lst : chunks ) for ( Chunk chunk : lst ) ret.add(chunk);
        return ret;
    }

    public void saveChunks(Path path) throws IOException  { 
        for ( Chunk chunk : getChunks() ) cacheChunk(chunk); 
        for ( String pos : chunkJSON.keySet() ) {
            int x = Integer.parseInt(pos.split(":")[0]);
            int y = Integer.parseInt(pos.split(":")[1]);
            Files.write(path.resolve(Paths.get("chunk"+String.format("%03d", x) + String.format("%03d", y)+ ".msv")),
                chunkJSON.get(pos).getBytes());
        }
    }
    public void saveChunk(Path path, Chunk chunk) throws IOException {
        if ( chunk == null ) return;
        cacheChunk(chunk);
        Files.write(path.resolve(Paths.get("chunk"+String.format("%03d", chunk.getXChunkPosition())
            + String.format("%03d", chunk.getYChunkPosition())+".msv")),
            chunk.save(new ParserBlock()).toString().getBytes());
    }
    private void cacheChunk(Chunk chunk) {
        if ( chunk == null ) return;
        chunkJSON.put(chunk.getXChunkPosition()+":"+chunk.getYChunkPosition(), 
            chunk.save(new ParserBlock()).toString()); 
    }
    
    public int getChunkSize() { return chunkSize; }
    public int getTileSize() { return tileSize; }
    public int getChunkLoadDiameter() { return chunkLoadDiameter; }
    public ArrayList<ArrayList<Chunk>> getActiveChunks() { return chunks; }
}
