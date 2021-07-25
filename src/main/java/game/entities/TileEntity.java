package game.entities;

import game.main.render.Renderer;
import game.main.render.Animation;
import game.main.X;
import game.main.TickObserver;
import game.physics.*;
import util.parse.obj.*;

import java.util.ArrayList;
import java.nio.file.Paths;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Separate entity to represent the tiles that make up the floor. This is a special entity due to the fact that its position is static and discrete within a chunk
 * and can therefore be stored in a simple array to allow for quick indexing unlike other entities.
 */
public class TileEntity extends Entity {
    private int type;
    private int chunkRow;
    private int chunkColumn;
    private boolean liquid;
    private Animation animation;
    private TileSpawnManager spawnManager;

    private TileEntity () {}
    public TileEntity(X x, ParserBlock block, Chunk chunk, int chunkRow, int chunkColumn) {
        this.size = new Point(chunk.getTileSize(), chunk.getTileSize());
        this.position = new Point2D.Double((chunk.getXChunkPosition()*chunk.getSizeInTiles() + chunkColumn)
            * (int) this.size.getX(), (chunk.getYChunkPosition()*chunk.getSizeInTiles() + chunkRow)
            * (int) this.size.getX());
        this.chunkRow = chunkRow;
        this.chunkColumn = chunkColumn;
        this.type = ((ParserInt) block.getProperty("type")).getNumber();
        this.liquid = block.getProperties().containsKey("liquid") ? 
            ((ParserInt) block.getProperty("liquid")).getNumber() == 1 : false;
        this.animation = new Animation(x, this, Paths.get("src/main/resources/tiles/" + this.type + ".png"));
        if ( block.getProperties().containsKey("collisionBoxes") ) 
            for (ParserObject obj : ((ParserArray) block.getProperty("collisionBoxes"))) 
                this.addCollisionBox(new CollisionBox(this, (ParserBlock) obj)); 
    }

    public void tick(X x) {
        animation.tick(x);
    }

    public void render(Renderer renderer) {
        renderer.render(animation);
    }

    public void addSpawnableEntity(Entity entity, double probability, int ticksUntilNextSpawn) {}
    public void addSpawnOnStartupEntity(Entity entity, double probability) {}
    public void removeSpawnableEntity(Entity entity) {}
    public void removeSpawnOnStartupEntity(Entity entity) {}
    
    public int getChunkRow() { return chunkRow; }
    public int getChunkColumn() { return chunkColumn; }
    public int getType() { return type; }
    public boolean isLiquid() { return liquid; }

    public ParserBlock save(ParserBlock block) {
        block.addProperty(new ParserProperty("type", new ParserInt(type)));
        block.addProperty(new ParserProperty("liquid", new ParserInt(liquid ? 1 : 0)));
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

