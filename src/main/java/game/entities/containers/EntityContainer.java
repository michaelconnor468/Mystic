package game.entities.containers;

import game.entities.Entity;
import game.player.Player;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.TickObserver;
import game.main.X;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Used to define an interface to containers that store entities. An interface is needed due to different indexing requirements for storing different types of 
 * entities efficiently, requiring each entity type to be stored within a unique data structure to most efficiently manage its query needs.
 */
public abstract class EntityContainer<E extends Entity> implements TickObserver, Renderable {
    protected int entityCount, maxEntitySize;
    private ArrayList<E> entities;
    private X x;

    protected EntityContainer() { this.entities = new ArrayList<>(); }
    public EntityContainer(X x) { this(); this.x = x; }

    /**
     * Re-indexing done upon addition of each entity in order to hide this implementation from external classes. Entities rarely cross chunks so
     * performance impact is negligible except for on loading which can be multi-threaded anyways.
     */
    public void addEntity( E entity ) { 
        entities.add(entity); 
        maxEntitySize = Math.max( maxEntitySize, Math.max(entity.getxSize(), entity.getySize()) );
        indexEntities();
    }
   
    // Does not require re-indexing since arraylist is used.
    public void removeEntity( E entity ) { entities.remove(entity); } 

    /**
     * Sorts array indexed by y position for fast queries and drawing. Not needed except on initialization and for dynamic entities that change their
     * y position of which there are not many in a given chunk making this efficient and worthwhile.
     */
    public void indexEntities() { 
        Collections.sort(entities, (e1, e2) -> e1.getyPosition() - e2.getyPosition()); 
    }

    public boolean isColliding(Entity entity) {
        if ( maxEntitySize == 0 ) return false;
        int minx = entity.getxPosition() - maxEntitySize*2;
        int miny = entity.getyPosition() - maxEntitySize*2;
        int maxx = entity.getxPosition() + entity.getxSize() + maxEntitySize*2;
        int maxy = entity.getyPosition() + entity.getySize() + maxEntitySize*2;
        for ( Entity e : getEntitiesWithinRange(minx, maxx, miny, maxy) ) 
            if ( e.isColliding(entity) ) return true;
        return false;
    }

    public void tick(X x) { for ( int i = entities.size(); i > 0; i-- ) entities.get(i-1).tick(x); }

    public void render(Renderer renderer) { 
        for ( int i = entities.size(); i > 0; i-- ) entities.get(i-1).render(renderer); 
    }
    public ArrayList<E> getAllEntities() { return (ArrayList<E>) entities.clone(); }
    public ArrayList<E> getEntitiesWithinRange(double minX, double maxX, double minY, double maxY) {
        ArrayList<E> ret = new ArrayList<E>();
        for ( int i = binarySearchFirstIndex( minY ); i > -1 && i < entities.size() && 
            entities.get(i).getyPosition() <= maxY; i++ )
            if ( entities.get(i).getxPosition() >= minX && entities.get(i).getxPosition() <= maxX ) 
                ret.add(entities.get(i));
        return ret;
    }

    private int binarySearchFirstIndex( double yPositionLow ) {
        if ( entities.size() == 0 ) return -1;
        int currentIndex = 0;
        int formerIndex = entities.size();
        while ( true ) {
            if ( formerIndex == currentIndex || currentIndex == 0 ) 
                return entities.get(currentIndex).getyPosition() >= yPositionLow ? 0 : -1;
            int half = (int) Math.abs( currentIndex - formerIndex ) / 2;
            formerIndex = currentIndex;
            currentIndex -= entities.get(currentIndex).getyPosition() >= yPositionLow ? half : -half; 
        }
    }

    public int getEntityCount() { return entityCount; } 
}
