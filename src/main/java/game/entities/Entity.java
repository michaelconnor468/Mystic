package game.entities;

import game.main.TickObserver;
import game.main.render.Renderable;
import game.main.render.Renderer;
import game.main.X;
import game.entities.buffs.Buff;
import util.parse.obj.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

/**
 * Abstract class for entity within the game. To allow easy addition of future features, everything in the game is considered an entity allowing it to
 * be synchronized by the tick and render timers so that any logic and animation of arbitrary features may be easily added in the future without changing 
 * the front end drivers.
 */
public abstract class Entity implements TickObserver, Renderable {
    protected X x;
    protected int xSize, ySize;
    protected int health;
    protected int maxHealth;
    protected boolean damageable;
    protected double xPosition, yPosition;
    protected ArrayList<CollisionBox> collisionBoxes;
    protected ArrayList<Buff> buffs; 

    protected Entity() { 
        this.collisionBoxes = new ArrayList<>();
        this.buffs = new ArrayList<>(); 
        this.damageable = false;
        this.maxHealth = 10;
        this.health = 10;
    }

    public synchronized void tick(X x) { 
        ArrayList<Buff> newBuffs = new ArrayList<>();
        for ( Buff buff : buffs ) {
            buff.tick(x);
            if ( buff.getTicksToLive() > 0 )
                newBuffs.add(buff);
        }
        buffs = newBuffs;
    }

    public void damage( double health ) {
        if ( !damageable ) return;
        this.health -= health;
        if ( this.health <= 0 )
            onDestroy();
    }

    public void heal( double health ) {
        this.health += health;
        if ( this.health > maxHealth )
            this.health = maxHealth;
    }

    public void onDestroy() {
        x.getChunkManager().getChunkInsideOf(this).removeEntity(this);
    }

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

    public synchronized void addBuff(Buff buff) { 
        for ( Buff currentBuff : buffs )
            if ( currentBuff.getName().equals(buff.getName()) ) return;
        buffs.add(buff); 
    }
    public boolean hasBuff(String str) {
        for ( Buff buff : buffs )
            if ( buff.getName().equals(str) ) return true;
        return false;
    }
    public synchronized void removeBuff(String str) {
        for ( Buff buff : buffs )
            if ( buff.getName().equals(str) ) buff.setTicksToLive(0);
    }

    public int getxPosition() { return (int) xPosition; }
    public int getyPosition() { return (int) yPosition; }
    public int getxSize() { return xSize; } 
    public int getySize() { return ySize; }  
    public double getHealth() { return this.health; }
    public double getMaxHealth() { return this.maxHealth; }
    public ArrayList<CollisionBox> getCollisionBoxes() { return this.collisionBoxes; }

    /**
     * Exists instead of a static loader because non-static inner classes in java do not play well with static methods.
     */
    public void addCollisionBox(X x, ParserBlock block) {
        CollisionBox box = new CollisionBox();
        box.xMin = ((ParserInt) block.getProperties().get("xMin")).getNumber();
        box.yMin = ((ParserInt) block.getProperties().get("yMin")).getNumber();
        box.xMax = ((ParserInt) block.getProperties().get("xMax")).getNumber();
        box.yMax = ((ParserInt) block.getProperties().get("yMax")).getNumber();
        if ( this.collisionBoxes == null )
            this.collisionBoxes = new ArrayList<>();
        this.collisionBoxes.add(box);
    }

    public boolean isColliding(Entity entity) {
        if ( entity.getCollisionBoxes() == null || collisionBoxes == null )
            return false;
        for ( CollisionBox box : collisionBoxes ) {
            for ( CollisionBox box2 : entity.getCollisionBoxes() )
                if ( box.collidesWith(box2) ) return true;
        }
        return false; 
    }

    /**
     * Represents the collision box for the entity. This data structure allows for storage in an array giving the 
     * ability to define multiple separate collision boxes for
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
