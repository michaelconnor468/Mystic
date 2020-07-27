package game.entities.containers;

import java.util.ArrayList;
import game.entities.Entity;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.timing.TickObserver;
import game.main.X;

/**
 * Used to define an interface to containers that store entities. An interface is needed due to different indexing requirements for storing different types of 
 * entities efficiently, requiring each entity type to be stored within a unique data structure to most efficiently manage its query needs.
 */
public abstract class EntityContainer<E extends Entity> implements TickObserver, Renderable {
    protected int entityCount;

    abstract public void addEntity( E entity );
    abstract public void removeEntity( E entity );
    abstract public void tick(X x);
    abstract public void render(Renderer renderer);
    abstract public ArrayList<E> getAllEntities();
    abstract public ArrayList<E> getEntitiesWithinRange(double minX, double maxX, double minY, double maxY);

    public int getEntityCount() {
        return entityCount;
    } 
}