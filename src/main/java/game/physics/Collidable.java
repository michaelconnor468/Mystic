package game.physics;

import java.awt.Point;
import java.util.Collection;

/**
 * Used by entities primarily for restricting movement between themselves and also for easily implementing
 * event based logic when two such entities collide. Whether or not a collision box can be passed through
 * or not is defferred to the collision box itself as opposed to the implementer of the interface for greater
 * flexibility allowing for intermingling of movemente and event based collision boxes within the same entity.
 */
public interface Collidable extends Positionable {
    public void onCollision(Collidable entity);
    public boolean isColliding();
    public void addCollisionBox(Point min, Point max);
    public Collection<CollisionBox> getCollisionBoxes();
}
