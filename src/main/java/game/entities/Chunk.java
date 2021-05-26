package game.entities;

import util.parse.obj.*;
import game.entities.containers.*;
import game.main.TickObserver;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.ChunkManager;
import game.main.X;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 * Represents a logical storage unit that consists of a NxN box of tiles and the entities stored within. This is used to boost performance and
 * scalability by providing discrete chunks which are active within the game and can be easily swapped with each other; only having several performing
 * processing and rendering on entities at a tiem. This decouples the game class from much of the implementation that can be driven through a simple and
 * lightweight interface.
 */
public class Chunk implements TickObserver, Renderable {
    private int sizeInTiles;
    private int xChunkPosition;
    private int yChunkPosition;
    private int tileSize;
    private ChunkManager chunkManager;

    // Use load method to instantiate object
    private Chunk() {}

    // Linked list provides layering, with each succesive index to be atop the other
    private ArrayList<TileEntityContainer> tileEntities;
    private ArrayList<StaticEntityContainer> staticEntities;
    private ArrayList<DynamicEntityContainer> dynamicEntities;
    private ArrayList<Entity> collidableEntities;

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
    public int getSizeInTiles() { return sizeInTiles; }

    public boolean isColliding(Entity entity) {
        for ( TileEntityContainer container : tileEntities ) 
            if ( container.isColliding(entity) ) return true;
            
        return false;
    }

    public void removeEntity(Entity entity) {
        if ( entity instanceof StaticEntity )
            for ( StaticEntityContainer container : staticEntities ) container.removeEntity((StaticEntity) entity);
        if ( entity instanceof DynamicEntity )
            for ( DynamicEntityContainer container : dynamicEntities ) container.removeEntity((DynamicEntity) entity);
    }

    public ArrayList<TileEntity> getTilesAround( Entity entity ) {
        ArrayList<TileEntity> ret = new ArrayList<>();
        for ( TileEntityContainer container : tileEntities )
            for ( TileEntity tileEntity : container.getEntitiesWithinRange( 
                entity.getxPosition(), entity.getxPosition() + entity.getxSize(), 
                    entity.getyPosition(), entity.getyPosition() + entity.getySize() ) )
                ret.add(tileEntity);
        return ret;
    }
    
    public static Chunk load(X x, ParserBlock block, int xPosition, int yPosition) {
        Chunk chunk = new Chunk();
        chunk.chunkManager = x.getChunkManager();
        chunk.tileSize = chunk.chunkManager.getTileSize();
        chunk.sizeInTiles = chunk.chunkManager.getChunkSize();
        chunk.tileEntities = new ArrayList<TileEntityContainer>();

        HashMap<String, ParserObject> properties = ((ParserBlock) block.getProperties().get("chunk")).getProperties();
        chunk.xChunkPosition = xPosition; 
        chunk.yChunkPosition = yPosition;

        TileEntityContainer tileEntityContainer = new TileEntityContainer(x);
        ParserArray tileRows = (ParserArray) properties.get("tileEntities");

        for ( int i = 0; i < tileRows.getLength(); i++ ) {
            ParserArray tileColumn = (ParserArray) tileRows.getIndex(i);
            for ( int ii = 0; ii < tileColumn.getLength(); ii++ ) { 
                ParserBlock entityBlock = (ParserBlock) tileColumn.getIndex(ii);
                TileEntity entity = TileEntity.load(x, entityBlock, chunk, i, ii);
                if ( entityBlock.getProperties().containsKey("collisionBoxes") ) 
                    for (ParserObject obj : ((ParserArray) entityBlock.getProperties().get("collisionBoxes"))) 
                        entity.addCollisionBox(x, (ParserBlock) obj); 
                tileEntityContainer.addEntity(entity);
            }
        }

        StaticEntityContainer staticEntityContainer = new StaticEntityContainer(x);
        ParserArray staticEntities = (ParserArray) properties.get("staticEntities");

        for ( int i = 0; i < staticEntities.getLength(); i++ ) {
            ParserBlock staticBlock = (ParserBlock) staticEntities.getIndex(i);
            StaticEntity staticEntity = StaticEntity.load(x, staticBlock);
            // TODO put entity into container after implementing load
        }

        chunk.tileEntities.add(tileEntityContainer);
        return chunk;
    }

    public static ParserBlock save(Chunk chunk) {
        // TODO
        ParserBlock block = new ParserBlock();
        return block;
    }
}
