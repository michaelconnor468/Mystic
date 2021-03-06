package game.physics;

import util.parse.obj.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.awt.Point;
import java.awt.geom.Point2D;

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
                ((ParserInt) block.getProperties().get("yMin")).getNumber()),
            new Point(((ParserInt) block.getProperties().get("xMax")).getNumber(),
                ((ParserInt) block.getProperties().get("yMax")).getNumber()),
            block.getProperties().containsKey("passable") &&
                ((ParserInt) block.getProperties().get("passable")).getNumber() == 1
        );
    }

    public CollisionBox(Collidable entity, Point min, Point max, boolean passable) {
        assert min.getX() <= max.getX() && min.getY() <= max.getY() : 
            "CollisionBox maximum point " + max + " must be greater than or equal to minimum point " + min;
        this.min = new Point(min);
        this.max = new Point(max);
        this.entity = entity;
        this.passable = passable;
    }

    /**
     * Informs both collidable entities when collision occurs although will return false if passable is set to true.
     * Entities may not collide with themselves as then this would always return true.
     */
    private boolean testCollisions(CollisionBox collisionBox) {
        if ( this.entity == collisionBox.entity ) return false;
        boolean collides = !(
            this.getRealMax().getX() < collisionBox.getRealMin().getX() ||
            this.getRealMin().getX() > collisionBox.getRealMax().getX() ||
            this.getRealMax().getY() < collisionBox.getRealMin().getY() ||
            this.getRealMin().getY() > collisionBox.getRealMax().getY() 
        );
        if ( collides ) {
            this.entity.onCollision(collisionBox.entity);
            collisionBox.entity.onCollision(this.entity);
        }
        return collides && !passable;
    }

    public Point2D.Double getRealMin() { 
        return new Point2D.Double(entity.getPosition().getX() + min.getX(), entity.getPosition().getY() + min.getY()); 
    }

    public Point2D.Double getRealMax() { 
        return new Point2D.Double(entity.getPosition().getX() + max.getX(), entity.getPosition().getY() + max.getY()); 
    }

    public ParserBlock save(ParserBlock block) {
        HashMap<String, ParserObject> boxProps = block.getProperties();
        boxProps.put("xMin", new ParserInt((int) min.getX()));
        boxProps.put("yMin", new ParserInt((int) min.getY()));
        boxProps.put("xMax", new ParserInt((int) max.getX()));
        boxProps.put("yMax", new ParserInt((int) max.getY()));
        return block;
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
