package game.physics;

import java.awt.Point;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Used by entities primarily for restricting movement between themselves and also for easily implementing
 * event based logic when two such entities collide. Whether or not a collision box can be passed through
 * or not is defferred to the collision box itself as opposed to the implementer of the interface for greater
 * flexibility allowing for intermingling of movemente and event based collision boxes within the same entity.
 */
public interface Collidable extends Positionable {
    default public void onCollision(Collidable entity) {}
    default public boolean testCollision(Collidable entity) { 
        return CollisionBox.testCollisions(getCollisionBoxes(), entity.getCollisionBoxes());
    }
    default public void addCollisionBox(CollisionBox box) {}
    default public Collection<CollisionBox> getCollisionBoxes() { return new ArrayList<>(); }
}
