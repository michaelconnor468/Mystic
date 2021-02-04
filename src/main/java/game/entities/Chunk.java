package game.entities;

import util.parse.obj.ParserBlock;
import java.util.LinkedList;
import java.util.Iterator;
import game.entities.containers.*;
import game.main.timing.TickObserver;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.X;

/**
 * Represents a logical storage unit that consists of a NxN box of tiles and the entities stored within. This is used to boost performance and
 * scalability by providing discrete chunks which are active within the game and can be easily swapped with each other; only having several performing
 * processing and rendering on entities at a tiem. This decouples the game class from much of the implementation that can be driven through a simple and
 * lightweight interface.
 */
public class Chunk implements TickObserver, Renderable, Saveable {
    private int sizeInTiles;
    private int xChunkPosition;
    private int yChunkPosition;
    private int tileSize;

    // Constructor ensures object cannot be initialized without required constant data
    public Chunk(int xChunkPosition, int yChunkPosition, int tileSize, int sizeInTiles) {
        this.xChunkPosition = xChunkPosition;
        this.yChunkPosition = yChunkPosition;
        this.tileSize = tileSize;
        this.sizeInTiles = sizeInTiles;
    }

    // Linked list provides layering, with each succesive index to be atop the other
    private LinkedList<TileEntityContainer> tileEntities;
    private LinkedList<StaticEntityContainer> staticEntities;
    private LinkedList<DynamicEntityContainer> dynamicEntities;
    private LinkedList<Entity> collidableEntities;

    public void tick(X x) {
        for ( TileEntityContainer container : tileEntities )
            container.tick(x);
        for ( StaticEntityContainer container : staticEntities )
            container.tick(x);
        for ( DynamicEntityContainer container : dynamicEntities )
            container.tick(x);
    }

    public void render(Renderer renderer) {
        for ( TileEntityContainer container : tileEntities )
            container.render(renderer);
        for ( StaticEntityContainer container : staticEntities )
            container.render(renderer);
        for ( DynamicEntityContainer container : dynamicEntities )
            container.render(renderer);
    }

    public int getXChunkPosition() { return xChunkPosition; }
    public int getYChunkPosition() { return yChunkPosition; }
    public int getTileSize() { return tileSize; }
    public int getTileRowDimension() { return sizeInTiles; }
    public int getTileColumnDimension() { return sizeInTiles; }
    
    public void load(ParserBlock block) {
        
    }

    public ParserBlock save() {
        ParserBlock block = new ParserBlock();
        return block;
    }
}
