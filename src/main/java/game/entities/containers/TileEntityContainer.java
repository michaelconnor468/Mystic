package game.entities.containers;

import game.entities.Chunk;
import game.entities.TileEntity;
import game.entities.Entity;
import game.main.render.Renderer;
import game.main.X;
import util.parse.obj.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.lang.Math;
import java.awt.Point;

/**
 * Stores tile entities in a simple matrix due to their discrete nature allowing such a structure to be easily indexed and queried with relation to position perameters. Since
 * all tiles in a chunk must always be present, data structure is always full with defaults where needed and elements can only be replaced with others.
 */
public class TileEntityContainer extends EntityContainer<TileEntity> {
    private ArrayList<ArrayList<TileEntity>> tileEntities;
    private int chunkSize;
    private int tileSize;
    private TileEntityContainer() {}

    public TileEntityContainer( X x ) {
        super(x);
        this.chunkSize = x.getChunkManager().getChunkSize();
        this.tileSize = x.getChunkManager().getTileSize();
        this.tileEntities = new ArrayList<ArrayList<TileEntity>>();
        for ( int i = 0; i < chunkSize; i++ )
            this.tileEntities.add(new ArrayList<TileEntity>());
    }

    public void addEntity( TileEntity tileEntity ) {
        assert tileEntity.getChunkRow() < chunkSize && tileEntity.getChunkColumn() < chunkSize; 
        tileEntities.get(tileEntity.getChunkRow()).add(tileEntity.getChunkColumn(), tileEntity);
    }

    public ArrayList<TileEntity> getAllEntities() {
        ArrayList<TileEntity> returnArrayList = new ArrayList<>();
        Iterator<ArrayList<TileEntity>> outerIterator = tileEntities.iterator();
        while ( outerIterator.hasNext() ) {
            Iterator<TileEntity> innerIterator = outerIterator.next().iterator();
            while ( innerIterator.hasNext() ) {
                returnArrayList.add(innerIterator.next());
            }
        }
        return returnArrayList;
    }

    /**
     * In practice this provides a good statistical average runtime as chunk size is small and most queries involve a 
     * small circular area around the entity which gives this a consistant low runtime.
     */
    public ArrayList<TileEntity> getEntitiesWithinRange( double minX, double maxX, double minY, double maxY ) {
        ArrayList<TileEntity> returnList = new ArrayList<>();
        for ( int i = 0; i < chunkSize; i++ ) {
            if ( tileEntities.get(i).get(0).getPosition().getY() >= minY && tileEntities.get(i).get(0).getPosition().getY() <= maxY )
            {
                for ( int j = 0; j < chunkSize; j++ ) {
                    if ( tileEntities.get(i).get(j).getPosition().getX() >= minX 
                        && tileEntities.get(i).get(j).getPosition().getX() <= maxX ) 
                        returnList.add( tileEntities.get(i).get(j) );
                }
            }
        }
        return returnList;
    }

    public TileEntity getEntityAt(int row, int column) { return tileEntities.get(row).get(column); }

    /**
     * Since there must always be a tile in the matrix for a chunk, method cannot actually remove it but replaces it with 
     * the default.
     * 
     * @param entity - entity to replace with the set default
     */
    public void removeEntity( TileEntity tileEntity ) {
        assert tileEntity.getChunkRow() < chunkSize && tileEntity.getChunkColumn() < chunkSize; 
        tileEntities.get(tileEntity.getChunkRow()).add(tileEntity.getChunkColumn(), null);
    }

    public boolean isColliding(Entity entity) {
        Point min = new Point((int) entity.getPosition().getX() - tileSize*2, 
            (int) entity.getPosition().getY() - tileSize*2);
        Point max = new Point((int) entity.getPosition().getX() + (int) entity.getSize().getX() + tileSize*2, 
            (int) entity.getPosition().getY() + (int) entity.getSize().getY() + tileSize*2);
        for ( TileEntity e : getEntitiesWithinRange(min, max) )
            if ( e.collidesWith(entity) ) return true;
        return false;
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
}
