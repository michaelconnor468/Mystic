package game.entities.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.lang.Math;
import game.entities.Chunk;
import game.entities.TileEntity;
import game.main.render.Renderer;
import game.main.X;

/**
 * Stores tile entities in a simple matrix due to their discrete nature allowing such a structure to be easily indexed and queried with relation to position perameters. Since
 * all tiles in a chunk must always be present, data structure is always full with defaults where needed and elements can only be replaced with others.
 */
public class TileEntityContainer extends EntityContainer<TileEntity> {
    ArrayList<ArrayList<TileEntity>> tileEntities;
    private int chunkSize;
    
    private TileEntityContainer() {}

    /**
     * Populates data structure with clones of default entity instead of a single one to allow future dynamic functionality between different ones to be implemented
     * which requires them to be distinguishable. Passing in an object as opposed to a class to create from allows for any pre-initialization of the default
     * object to be implemented outside the container.
     * 
     * @param defaultEntity - entity to be cloned and used as a default where there is no other added
     */
    public TileEntityContainer( Chunk chunk ) {
        this.chunkSize = chunk.getSizeInTiles();
        this.tileEntities = new ArrayList<ArrayList<TileEntity>>(Collections.nCopies(chunkSize, 
            new ArrayList<TileEntity>(Collections.nCopies(chunkSize, null))));
    }

    /**
     * Since tile matrix always has its slots filled with default or otherwise, this does not add to it, but merely replaces the tile entity in its place.
     * 
     * @param entity - entity to replace currently stored one with
     */
    public void addEntity( TileEntity tileEntity ) {
        assert tileEntity.getChunkRow() < chunkSize && tileEntity.getChunkColumn() < chunkSize : "TileEntityContainer can only store TileEntities of its given dimenstions. TileEntityString: " + tileEntity.toString();
        tileEntities.get(tileEntity.getChunkRow()).add(tileEntity.getChunkColumn(), tileEntity);
    }

    public ArrayList<TileEntity> getAllEntities() {
        ArrayList<TileEntity> returnArrayList = new ArrayList<>();
        Iterator<ArrayList<TileEntity>> outerIterator = tileEntities.iterator();
        while ( outerIterator.hasNext() ) {
            Iterator<TileEntity> innerIterator = outerIterator.next().iterator();
            while (innerIterator.hasNext() ) {
                returnArrayList.add(innerIterator.next());
            }
        }
        return returnArrayList;
    }

    /**
     * Algorithm used to implement this seems rather naive at first glance, however in practice this provides a good statistical average as chunk size is small
     * and most queries involve a small circular area around the entity which gives this a consistant low runtime.
     */
    public ArrayList<TileEntity> getEntitiesWithinRange( double minX, double maxX, double minY, double maxY ) {
        ArrayList<TileEntity> returnList = new ArrayList<>();
        for ( int i = 0; i < chunkSize; i++ ) {
            if ( tileEntities.get(i).get(0).getyPosition() >= minY && tileEntities.get(i).get(0).getyPosition() <= maxY ) {
                for ( int j = 0; j < chunkSize; j++ ) {
                    if ( tileEntities.get(i).get(j).getxPosition() >= minX && tileEntities.get(i).get(j).getxPosition() <= maxX )
                        returnList.add( tileEntities.get(i).get(j) );
                }
            }
        }
        return returnList;
    }

    /**
     * Since there must always be a tile in the matrix for a chunk, method cannot actually remove it but replaces it with the default.
     * 
     * @param entity - entity to replace with the set default
     */
    public void removeEntity( TileEntity tileEntity ) {
        assert tileEntity.getChunkRow() < chunkSize && tileEntity.getChunkColumn() < chunkSize : "TileEntityContainer can only store TileEntities of its given dimenstions" + tileEntity.toString();
        tileEntities.get(tileEntity.getChunkRow()).add(tileEntity.getChunkColumn(), null);
    }

    public void tick( X x ) {
        for ( ArrayList<TileEntity> entityList : tileEntities ) {
            for ( TileEntity entity : entityList ) {
                entity.tick(x);
            }
        }
    }

    public void render( Renderer renderer ) {
        for ( ArrayList<TileEntity> entityList : tileEntities ) {
            for ( TileEntity entity : entityList ) {
                entity.render(renderer);
            }
        }
    }

    public int getEntityCount() {
        return entityCount;
    }
}
