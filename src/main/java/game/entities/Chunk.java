package game.entities;

import util.parse.obj.*;
import game.entities.containers.*;
import game.main.TickObserver;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.ChunkManager;
import game.physics.*;
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

    private Chunk() {}
    public Chunk(X x, ParserBlock block, int xPosition, int yPosition) {
        this.chunkManager = x.getChunkManager();
        this.tileSize = this.chunkManager.getTileSize();
        this.sizeInTiles = this.chunkManager.getChunkSize();
        this.xChunkPosition = xPosition; 
        this.yChunkPosition = yPosition;
        
        HashMap<String, ParserObject> properties = ((ParserBlock) block.getProperties().get("chunk")).getProperties();

        TileEntityContainer tileEntityContainer = new TileEntityContainer(x);
        ParserArray tileRows = (ParserArray) properties.get("tileEntities");
        for ( int i = 0; i < tileRows.getLength(); i++ ) {
            ParserArray tileColumn = (ParserArray) tileRows.getIndex(i);
            for ( int ii = 0; ii < tileColumn.getLength(); ii++ ) 
                tileEntityContainer.addEntity(new TileEntity(x, (ParserBlock) tileColumn.getIndex(ii), this, i, ii));
        }
        this.tileEntities = tileEntityContainer;

        StaticEntityContainer staticEntityContainer = new StaticEntityContainer(x);
        ParserArray staticEntityArray = (ParserArray) properties.get("staticEntities");
        for ( int i = 0; i < staticEntityArray.getLength(); i++ ) 
            staticEntityContainer.addEntity(new StaticEntity(x, (ParserBlock) staticEntityArray.getIndex(i)));
        this.staticEntities = staticEntityContainer;

        DynamicEntityContainer dynamicEntityContainer = new DynamicEntityContainer(x);
        this.dynamicEntities = dynamicEntityContainer;
    }

    private TileEntityContainer tileEntities;
    private StaticEntityContainer staticEntities;
    private DynamicEntityContainer dynamicEntities;

    public void tick(X x) {
        tileEntities.tick(x);
        staticEntities.tick(x);
        dynamicEntities.tick(x);
    }

    public void render(Renderer renderer) {
        tileEntities.render(renderer);
        staticEntities.render(renderer);
        dynamicEntities.render(renderer);
    }

    public int getXChunkPosition() { return xChunkPosition; }
    public int getYChunkPosition() { return yChunkPosition; }
    public int getTileSize() { return tileSize; }
    public int getSizeInTiles() { return sizeInTiles; }

    public boolean testCollision(Entity entity) {
        return ( tileEntities.testCollision(entity) || staticEntities.testCollision(entity) 
            || dynamicEntities.testCollision(entity) );
    }

    public void removeEntity(Entity entity) {
        if ( entity instanceof StaticEntity ) staticEntities.removeEntity((StaticEntity) entity);
        if ( entity instanceof DynamicEntity ) dynamicEntities.removeEntity((DynamicEntity) entity);
    }

    public void addEntity(Entity entity) {
        if ( entity instanceof StaticEntity ) staticEntities.addEntity((StaticEntity) entity);
        if ( entity instanceof DynamicEntity ) dynamicEntities.addEntity((DynamicEntity) entity);
    }

    public ArrayList<TileEntity> getTilesAround( Entity entity ) {
        ArrayList<TileEntity> ret = new ArrayList<>();
        for ( TileEntity tileEntity : tileEntities.getEntitiesWithinRange( 
            entity.getPosition().getX(), entity.getPosition().getX() + entity.getSize().getX(), 
                entity.getPosition().getY(), entity.getPosition().getY() + entity.getSize().getY() ) )
                    ret.add(tileEntity);
        return ret;
    }
    
    public ParserBlock save(ParserBlock block) {
        ParserBlock chunkBlock = new ParserBlock();

        tileEntities.save(chunkBlock);
        staticEntities.save(chunkBlock);

        block.addProperty(new ParserProperty("chunk", chunkBlock));
        return block;
    }
}
