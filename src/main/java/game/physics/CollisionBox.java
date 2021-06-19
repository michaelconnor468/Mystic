package game.physics;

import util.parse.obj.*;

import java.util.Map;
import java.util.Collection;
import java.awt.Point;

/**
 * For entities implementing collidable. Relatively situated within an entity for easy saving and loading and to reduce
 * any possible errors that come with having to constantly update any absolute positions atomically with that of the player.
 * Collision boxes are not thread safe when both entities are dynamic and hence two such entities should not be split off
 * into separate threads unless further synchronization measures are taken.
 */
public class CollisionBox {
    private Point min, max;
    private Collidable entity;
    private boolean passable;

    private CollisionBox() {}

    public CollisionBox(Collidable entity, ParserBlock block) {
        this(entity, 
            new Point(((ParserInt) block.getProperties().get("xMin")).getNumber(), 
                ((ParserInt) block.getProperties().get("xMax")).getNumber()),
            new Point(((ParserInt) block.getProperties().get("yMin")).getNumber(), 
                ((ParserInt) block.getProperties().get("yMax")).getNumber()),
            ((ParserInt) block.getProperties().get("passable")).getNumber() == 1
        );
    }

    public CollisionBox(Collidable entity, Point min, Point max, boolean passable) {
        assert min.getX() < max.getX() && min.getY() < max.getY() : "CollisionBox maximum point must be greater.";
        this.min = new Point(min);
        this.max = new Point(max);
        this.entity = entity;
        this.passable = passable;
    }

    /**
     * Informs both collidable entities when collision occurs although will return false if passable is set to true.
     */
    private boolean testCollisions(CollisionBox collisionBox) {
        boolean collides = !(
            this.getRealMax().getX() < collisionBox.getRealMin().getX() ||
            this.getRealMin().getX() > collisionBox.getRealMax().getX() ||
            this.getRealMax().getY() < collisionBox.getRealMin().getY() ||
            this.getRealMin().getY() > collisionBox.getRealMax().getY() 
        );
        if ( collides ) {
            entity.onCollision(collisionBox.getCollidableEntity());
            collisionBox.getCollidableEntity().onCollision(entity);
        }
        return collides && !passable;
    }

    public Point getRealMin() { 
        Point ret = new Point(entity.getPosition()); 
        ret.translate((int) min.getX(), (int) min.getY()); 
        return ret;
    }

    public Point getRealMax() { 
        Point ret = new Point(entity.getPosition()); 
        ret.translate((int) max.getX(), (int) max.getY()); 
        return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("xMin: " + min.getX());
        sb.append(", xMax: " + max.getX());
        sb.append(", yMin: " + min.getY());
        sb.append(", yMax: " + max.getY());
        sb.append(", passable: " + ((passable) ? 1 : 0));
        sb.append("}");
        return sb.toString();
    };

    public static boolean testCollisions(Collection<CollisionBox> boxes1, Collection<CollisionBox> boxes2) {
        for ( CollisionBox box1 : boxes1 ) 
            for ( CollisionBox box2 : boxes2 ) 
                if ( box1.testCollisions(box2) ) return true;
        return false;
    }

    public Collidable getCollidableEntity() { return entity; }
}
