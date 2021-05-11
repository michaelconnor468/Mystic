package game.entities;

import game.main.render.Renderer;
import game.main.X;
import game.main.TickObserver;
import util.parse.obj.*;

import java.util.ArrayList;

/**
 * Separate entity to represent the tiles that make up the floor. This is a special entity due to the fact that its position is static and discrete within a chunk
 * and can therefore be stored in a simple array to allow for quick indexing unlike other entities.
 */
public class TileEntity extends Entity {
    private int type;
    private int chunkRow;
    private int chunkColumn;
    TileSpawnManager spawnManager;

    private TileEntity () {}
    
    public static TileEntity load(X x, ParserBlock block, Chunk chunk, int chunkRow, int chunkColumn) {
        TileEntity entity = new TileEntity();
        entity.xSize = chunk.getTileSize();
        entity.ySize = chunk.getTileSize();
        entity.xPosition = (chunk.getXChunkPosition()*chunk.getSizeInTiles() + chunkRow)*entity.xSize;
        entity.yPosition = (chunk.getYChunkPosition()*chunk.getSizeInTiles() + chunkColumn)*entity.xSize;
        entity.chunkRow = chunkRow;
        entity.chunkColumn = chunkColumn;
        entity.type = ((ParserInt) block.getProperties().get("type")).getNumber();
        return entity;
    }

    public void tick(X x) {}

    public void render(Renderer renderer) {}

    public void addSpawnableEntity(Entity entity, double probability, int ticksUntilNextSpawn) {}
    public void addSpawnOnStartupEntity(Entity entity, double probability) {}
    public void removeSpawnableEntity(Entity entity) {}
    public void removeSpawnOnStartupEntity(Entity entity) {}
    
    public int getChunkRow() { return chunkRow; }
    public int getChunkColumn() { return chunkColumn; }

    public static ParserBlock save(TileEntity tile) {
        ParserBlock block = new ParserBlock();
        return block;
    }


    /**
     * Decides whether any entities should be created in a location within this tile on each tick. 
     */
    private class TileSpawnManager implements TickObserver {
        private ArrayList<Entity> spawnableEntities;
        private ArrayList<Entity> spawnOnStartup;
        private ArrayList<Double> spawnProbabilities;
        private ArrayList<Double> spawnOnStartupProbabilities;
        private ArrayList<Integer> ticksUntilNextSpawn;

        public void tick(X x) {

        }
    }
}

