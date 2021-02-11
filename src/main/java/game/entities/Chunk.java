package game.entities;

import util.parse.obj.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
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

    private Chunk() {}

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
    
    public static Chunk load(X x, ParserBlock block) {
        Chunk chunk = new Chunk();
        chunk.tileSize = x.tileSize;
        chunk.sizeInTiles = x.chunkSize;

        HashMap<String, ParserObject> properties = block.getProperties();
        chunk.xChunkPosition = ((ParserInt) properties.get("xLocation")).getNumber();
        chunk.yChunkPosition = ((ParserInt) properties.get("yLocation")).getNumber();

        ParserArray tileRows = (ParserArray) properties.get("tileEntities");
        for ( int i = 0; i < tileRows.getLength(); i++ ) {
            ParserArray tileColumn = (ParserArray) tileRows.getIndex(i);
            TileEntityContainer tileEntityContainer = new TileEntityContainer(chunk);
            for ( int ii = 0; ii < tileColumn.getLength(); ii++  ) {
                ParserBlock tileBlock = (ParserBlock) tileColumn.getIndex(ii);
                HashMap<String, ParserObject> tileProperties = tileBlock.getProperties();
                TileEntity entity = new TileEntity(x, chunk.tileSize, (chunk.xChunkPosition*chunk.sizeInTiles + ii)*chunk.tileSize, (chunk.yChunkPosition*chunk.sizeInTiles + i)*chunk.tileSize, i, ii, ((ParserInt) tileProperties.get("type")).getNumber(), ((ParserInt) tileProperties.get("biome")).getNumber());
                tileEntityContainer.addEntity(entity);
            }
            chunk.tileEntities.add(tileEntityContainer);
        }
        return chunk;
    }

    public ParserBlock save() {
        ParserBlock block = new ParserBlock();
        return block;
    }
}
