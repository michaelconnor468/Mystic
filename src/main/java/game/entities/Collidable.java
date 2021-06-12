package game.entities;

import java.awt.Point;
import java.util.Collection;

public interface Collidable extends Positionable {
    public void onCollision();
    public boolean isColliding();
    public void addCollisionBox(Point min, Point max);
    public Collection<CollisionBox> getCollisionBoxes();
}
