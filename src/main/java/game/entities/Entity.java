package game.entities;

import game.main.TickObserver;
import game.main.render.*;
import game.main.X;
import game.entities.buffs.Buff;
import game.player.weapons.Weapon;
import game.player.items.Item;
import util.parse.obj.*;
import util.parse.BlockParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.awt.Point;

/**
 * Abstract class for entity within the game. To allow easy addition of future features, everything in the game is considered an entity allowing it to
 * be synchronized by the tick and render timers so that any logic and animation of arbitrary features may be easily added in the future without changing 
 * the front end drivers.
 */
public abstract class Entity implements TickObserver, Renderable, Positionable {
    protected X x;
    protected String name;
    protected Point position;
    protected Point size;
    protected int health;
    protected int maxHealth;
    protected boolean damageable;
    protected boolean passable;
    protected ArrayList<CollisionBox> collisionBoxes;
    protected ArrayList<Buff> buffs; 
    protected ArrayList<Integer> drops; 
    protected Animation animation;

    protected Entity() { 
        this.collisionBoxes = new ArrayList<>();
        this.buffs = new ArrayList<>(); 
        this.damageable = false;
        this.maxHealth = 10;
        this.health = 10;
    }

    public synchronized void tick(X x) { 
        if ( animation != null ) this.animation.tick(x);
        ArrayList<Buff> newBuffs = new ArrayList<>();
        for ( Buff buff : buffs ) {
            buff.tick(x);
            if ( buff.getTicksToLive() > 0 )
                newBuffs.add(buff);
        }
        buffs = newBuffs;
    }

    public void render(Renderer renderer) {}

    public void damage( Weapon weapon ) {
        if ( !damageable ) return;
        this.health -= weapon.getPhysicalDamage();
        if ( this.health <= 0 )
            onDestroy();
    }

    public void heal( double health ) {
        this.health += health;
        if ( this.health > maxHealth )
            this.health = maxHealth;
    }

    public void onDestroy() {
        for ( int drop : drops ) Item.drop(x, drop, this);
        x.getChunkManager().getChunkInsideOf(this).removeEntity(this);
    }

    public void addCollisionBox(CollisionBox collisionBox) {collisionBoxes.add(collisionBox);}
    public void removeCollisionBox(CollisionBox collisionBox) {collisionBoxes.remove(collisionBox);}
    public void removeCollisionBoxes() {collisionBoxes = new ArrayList<>();}

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

    public Point getPosition() { return new Point(position); }
    public Point getSize() { return new Point(size); } 
    public double getHealth() { return this.health; }
    public double getMaxHealth() { return this.maxHealth; }
    public String getName() { return this.name; }
    public ArrayList<CollisionBox> getCollisionBoxes() { return this.collisionBoxes; }

    /**
     * Exists instead of a static loader because non-static inner classes in java do not play well with static methods.
     */
    public void addCollisionBox(ParserBlock block) {
        CollisionBox box = new CollisionBox();
        box.xMin = ((ParserInt) block.getProperties().get("xMin")).getNumber();
        box.yMin = ((ParserInt) block.getProperties().get("yMin")).getNumber();
        box.xMax = ((ParserInt) block.getProperties().get("xMax")).getNumber();
        box.yMax = ((ParserInt) block.getProperties().get("yMax")).getNumber();
        if ( this.collisionBoxes == null )
            this.collisionBoxes = new ArrayList<>();
        this.collisionBoxes.add(box);
    }

    public void addCollisionBox(int xmin, int xmax, int ymin, int ymax) {
        addCollisionBox((new BlockParser()).parse("{ xMin: "+xmin+" xMax: "+xmax+" yMin: "+ymin+" yMax: "+ymax+" }"));
    }

    public boolean isColliding(Entity entity) {
        if ( entity.getCollisionBoxes() == null || collisionBoxes == null )
            return false;
        for ( CollisionBox box : collisionBoxes ) {
            for ( CollisionBox box2 : entity.getCollisionBoxes() )
                if ( box.collidesWith(box2) ) {
                    onCollision(entity);
                    entity.onCollision(this);
                    return passable ? false : true;
                }
        }
        return false; 
    }

    public void onCollision(Entity entity) {}

    /**
     * Represents the collision box for the entity. This data structure allows for storage in an array giving the 
     * ability to define multiple separate collision boxes for
     * an entity for more fine-grained collision control
     */
    public class CollisionBox {
        // Corners relative to position of entity
        private double xMin, xMax, yMin, yMax;

        double getRealxMin() {return Entity.this.getPosition().getX() + xMin;}
        double getRealxMax() {return Entity.this.getPosition().getX() + xMax;}
        double getRealyMin() {return Entity.this.getPosition().getY() + yMin;}
        double getRealyMax() {return Entity.this.getPosition().getY() + yMax;}

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

        public String toString() {return "{xMin: "+xMin+" xMax: "+xMax+" yMin: "+yMin+" yMax: "+yMax+"}";}
    }
    
    protected static Entity load(X x, ParserBlock block, ParserBlock template, Entity entity) {
        entity.x = x;
        entity.maxHealth = ((ParserInt) loadProperty(block, template, "maxHealth")).getNumber();
        entity.health = ((ParserInt) loadProperty(block, template, "health")).getNumber();
        entity.name = ((ParserString) loadProperty(block, template, "name")).getString();
        int xSize = ((ParserInt) loadProperty(block, template, "xSize")).getNumber();
        int ySize = ((ParserInt) loadProperty(block, template, "ySize")).getNumber();
        int xPosition = ((ParserInt) loadProperty(block, template, "xPosition")).getNumber();
        int yPosition = ((ParserInt) loadProperty(block, template, "yPosition")).getNumber();
        entity.position = new Point(xPosition, yPosition);
        entity.size = new Point(xSize, ySize);
        if (loadProperty(block, template, "damageable") != null)
            entity.damageable = ((ParserInt) loadProperty(block, template, "damageable")).getNumber() == 1;
        for ( ParserObject box : ((ParserArray) loadProperty(block, template, "collisionBoxes")) )
            entity.addCollisionBox((ParserBlock) box);
        entity.drops = new ArrayList<>();
        for ( ParserObject i : ((ParserArray) loadProperty(block, template, "drops")) )
            entity.drops.add(((ParserInt) i).getNumber());
        return entity;
    }

    private static ParserObject loadProperty(ParserBlock block, ParserBlock template, String name) {
        HashMap<String, ParserObject> templateProps = template.getProperties();
        HashMap<String, ParserObject> props = block.getProperties();
        return props.containsKey(name) ? props.get(name) : templateProps.get(name);
    }
}
