package game.entities.containers;

import game.entities.Entity;
import game.player.Player;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.TickObserver;
import game.main.X;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

/**
 * Used to define an interface to containers that store entities. An interface is needed due to different indexing requirements for storing different types of 
 * entities efficiently, requiring each entity type to be stored within a unique data structure to most efficiently manage its query needs.
 */
public abstract class EntityContainer<E extends Entity> implements TickObserver, Renderable {
    protected int entityCount, maxEntitySize;
    protected ArrayList<E> entities;
    protected X x;

    protected EntityContainer() { this.entities = new ArrayList<>(); }
    public EntityContainer(X x) { this(); this.x = x; }

    /**
     * Re-indexing done upon addition of each entity in order to hide this implementation from external classes. Entities rarely cross chunks so
     * performance impact is negligible except for on loading which can be multi-threaded anyways.
     */
    public void addEntity( E entity ) { 
        entities.add(entity); 
        maxEntitySize = Math.max( maxEntitySize, Math.max((int) entity.getSize().getX(), (int) entity.getSize().getY()) );
        indexEntities();
    }
   
    // Does not require re-indexing since arraylist is used.
    public void removeEntity( E entity ) { entities.remove(entity); } 

    /**
     * Sorts array indexed by y position for fast queries and drawing. Not needed except on initialization and for dynamic entities that change their
     * y position of which there are not many in a given chunk making this efficient and worthwhile.
     */
    private void indexEntities() { 
        Collections.sort(entities, (e1, e2) -> (int) (e2.getPosition().getY() - e1.getPosition().getY())); 
    }

    public boolean testCollision(Entity entity) {
        if ( maxEntitySize == 0 ) return false;
        Point min = new Point((int) entity.getPosition().getX() - maxEntitySize*2,
            (int) entity.getPosition().getY() - maxEntitySize*2);
        Point max = new Point((int) entity.getPosition().getX() + (int) entity.getSize().getX() + maxEntitySize*2,
            (int) entity.getPosition().getY() + (int) entity.getSize().getY() + maxEntitySize*2);
        for ( Entity e : getEntitiesWithinRange(min, max) ) 
            if ( e.testCollision(entity) ) return true;
        return false;
    }

    public void tick(X x) { for ( int i = entities.size(); i > 0; i-- ) entities.get(i-1).tick(x); }

    public void render(Renderer renderer) {
        for ( int i = entities.size(); i > 0; i-- ) entities.get(i-1).render(renderer); 
    }
    public ArrayList<E> getAllEntities() { return (ArrayList<E>) entities.clone(); }
    public ArrayList<E> getEntitiesWithinRange(Point min, Point max) {
        indexEntities();
        ArrayList<E> ret = new ArrayList<E>();
        for ( int i = binarySearchFirstIndex( min.getY() - maxEntitySize ); i > -1 && i < entities.size() && entities.get(i).getPosition().getY() <= max.getY(); i++ )
            if (entities.get(i).getPosition().getX() + entities.get(i).getSize().getX() >= min.getX() && entities.get(i).getPosition().getX() <= max.getX())
                ret.add(entities.get(i));
        return ret;
    }

    private int binarySearchFirstIndex( double yPositionLow ) {
        if ( entities.size() == 0 ) return -1;
        int currentIndex = 0;
        int formerIndex = entities.size();
        while ( true ) {
            if ( formerIndex == currentIndex || currentIndex == 0 ) 
                return entities.get(currentIndex).getPosition().getY() >= yPositionLow ? 0 : -1;
            int half = (int) Math.abs( currentIndex - formerIndex ) / 2;
            formerIndex = currentIndex;
            currentIndex -= entities.get(currentIndex).getPosition().getY() >= yPositionLow ? half : -half; 
        }
    }

    public int getEntityCount() { return entityCount; } 
}
