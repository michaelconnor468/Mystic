package game.entities;

import util.parse.obj.*;
import game.main.render.Renderer;
import game.main.X;
import java.util.ArrayList;
import game.main.timing.TickObserver;


/**
 * Separate entity to represent the tiles that make up the floor. This is a special entity due to the fact that its position is static and discrete within a chunk
 * and can therefore be stored in a simple array to allow for quick indexing unlike other entities.
 */
public class TileEntity extends Entity implements Saveable {
    private int chunkRow;
    private int chunkColumn;
    Biome biome;
    TileSpawnManager spawnManager;

    public TileEntity(X x, int xSize, int ySize, int xPosition, int yPosition, int chunkRow, int chunkColumn) {
        super(x, xSize, ySize, xPosition, yPosition);
        this.chunkRow = chunkRow;
        this.chunkColumn = chunkColumn;
        assert xSize == ySize : "Tile Entities must have the same x and y size dimensions: " + this.toString();
    }

    public void tick(X x) {
        return;
    }

    public void render(Renderer renderer) {

    }

    public int getChunkRow() {
        return chunkRow;
    }

    public int getChunkColumn() {
        return chunkColumn;
    }

    public void addSpawnableEntity(Entity entity, double probability, int ticksUntilNextSpawn) {
        //TODO
    }
    public void addSpawnOnStartupEntity(Entity entity, double probability) {
        //TODO
    }

    public void removeSpawnableEntity(Entity entity) {
        //TODO
    }
    public void removeSpawnOnStartupEntity(Entity entity) {
        //TODO
    }
    
    public void load(ParserBlock block) {
        //TODO
    }

    public ParserBlock save() {
        ParserBlock block = new ParserBlock();
        return block;
    }

    /**
     * Decides whether any entities should be created in a location within this tile on each tick. Uses the api call to add an entity provided with the context object
     * in order to separate any entity management implementation from the act of spawning.
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

