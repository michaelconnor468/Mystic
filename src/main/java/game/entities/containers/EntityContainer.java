package game.entities.containers;

import game.entities.Entity;
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
    protected int entityCount;
    private ArrayList<E> entities;

    /**
     * Re-indexing done upon addition of each entity in order to hide this implementation from external classes. Entities rarely cross chunks so
     * performance impact is negligible except for on loading which can be multi-threaded anyways.
     */
    public void addEntity( E entity ) { 
        entities.add(entity); 
        indexEntities();
    }
    
    public void removeEntity( E entity ) { entities.remove(entity); } // Does not require re-indexing since arraylist is used.

    /**
     * Sorts array indexed by y position for fast queries and drawing. Not needed except on initialization and for dynamic entities that change their
     * y position of which there are not many in a given chunk making this efficient and worthwhile.
     */
    public void indexEntities() { 
        Collections.sort(entities, (e1, e2) -> e1.getyPosition() - e2.getyPosition()); 
    }

    public void tick(X x) { for ( E entity : entities ) entity.tick(x); }

    public void render(Renderer renderer) { for ( E entity : entities ) entity.render(renderer); }
    public ArrayList<E> getAllEntities() { return (ArrayList<E>) entities.clone(); }
    abstract public ArrayList<E> getEntitiesWithinRange(double minX, double maxX, double minY, double maxY);

    public int getEntityCount() {
        return entityCount;
    } 
}
