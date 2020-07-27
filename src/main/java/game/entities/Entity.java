package game.entities;

import game.main.timing.TickObserver;
import game.main.render.Renderable;
import game.main.render.Renderer;
import java.util.ArrayList;
import java.util.Iterator;
import game.main.X;
import game.entities.buffs.EntityBuff;

/**
 * Abstract class for entity within the game. To allow easy addition of future features, everything in the game is considered an entity allowing it to
 * be synchronized by the tick and render timers so that any logic and animation of arbitrary features may be easily added in the future without changing 
 * the front end drivers.
 * 
 * @implNote - xyPosition is the center of entity instead of any corners as that would introduce a bias in querying entities in a given area
 */
abstract public class Entity implements TickObserver, Renderable {
    // Size is not necessarily the same as the collision box dimensions
    protected int xSize, ySize;
    protected int xPosition, yPosition;
    protected ArrayList<CollisionBox> collisionBoxes;
    protected ArrayList<EntityBuff> entityBuffs;

    public Entity() {}

    public Entity(int xSize, int ySize, int xPosition, int yPosition) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    abstract public void tick(X x);
    abstract public void render(Renderer renderer);

    public void addCollisionBox(CollisionBox collisionBox) {collisionBoxes.add(collisionBox);}
    public void removeCollisionBox(CollisionBox collisionBox) {collisionBoxes.remove(collisionBox);}

    /**
     * Returns a deep clone of the entity in order to ensure changes to one clone do not impact the other
     */
    public Object clone() {
        try {
            Entity clone = (Entity) super.clone();
            ArrayList<CollisionBox> newBoxes = new ArrayList<CollisionBox>();
            Iterator<CollisionBox> iterator = ((ArrayList<CollisionBox>) clone.getCollisionBoxes()).iterator();
            while ( iterator.hasNext() ) {
                newBoxes.add((CollisionBox) iterator.next().clone());
            }
            clone.collisionBoxes = newBoxes;
            return clone;
        }
        catch (Exception e) {
            System.err.println("Failed to make a clone of entity object" + e.toString());
            return null;
        }
    }

    public void addEntityBuff(EntityBuff entityBuff) { entityBuffs.add(entityBuff); }

    public double getxPosition() {return xPosition;}
    public double getyPosition() {return yPosition;}
    public double getxSize() {return xSize;} 
    public double getySize() {return ySize;}  
    public double getActionButtonXLocation() {return xPosition;}
    public double getActionButtonYLocation() {return yPosition;}
    public ArrayList<CollisionBox> getCollisionBoxes() {return this.collisionBoxes;}

    /**
     * Represents the collision box for the entity. This data structure allows for storage in an array giving the ability to define multiple separate collision boxes for
     * an entity for more fine-grained collision control
     */
    public class CollisionBox {
        // Corners relative to position of entity
        private double xMin, xMax, yMin, yMax;

        double getRealxMin() {return Entity.this.getxPosition() + xMin;}
        double getRealxMax() {return Entity.this.getxPosition() + xMax;}
        double getRealyMin() {return Entity.this.getyPosition() + yMin;}
        double getRealyMax() {return Entity.this.getyPosition() + yMax;}

        private CollisionBox() {}

        public CollisionBox(double xMin, double xMax, double yMin, double yMax) {
            this();
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        public boolean collidesWith(CollisionBox collisionBox) {
            return !(
                (this.getRealxMax() < collisionBox.getRealxMin()) || 
                (this.getRealxMin() > collisionBox.getRealxMax()) || 
                (this.getRealyMax() < collisionBox.getRealyMin()) || 
                (this.getRealyMin() > collisionBox.getRealyMax())
            );
        }

        public Object clone() {
            try {
                return super.clone();
            }
            catch (Exception e) {
                System.err.println("Failed to make a clone of collision box object" + e.toString());
                return null;
            }
        }
    }

    public String toJSON() {
        return "";
    }
}