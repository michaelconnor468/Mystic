package game.entities.containers;

import java.util.ArrayList;
import java.util.Iterator;
import game.entities.Entity;

/**
 * Used to implement graph-like structures for making data structures that allow for more flexible and efficiend querries of entity objects.
 */
public class EntityContainerNode<E extends Entity> {
    E entity;
    double index;
    ArrayList<E> childEntities;

    public EntityContainerNode(double index, E entity) {
        this.index = index;
        this.entity = entity;
    }

    public void addChildEntity(E entity) {
        childEntities.add(entity);
    }

    public void removeChildEntity(E entity) {
        Iterator iterator = childEntities.iterator();
        while ( iterator.hasNext() ) {
            if ( iterator.next() == entity )
                iterator.remove();
        }
    }
}