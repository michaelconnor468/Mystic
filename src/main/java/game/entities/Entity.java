package game.entities;

import game.main.TickObserver;
import game.main.render.*;
import game.main.X;
import game.entities.buffs.Buff;
import game.player.weapons.Weapon;
import game.player.items.Item;
import game.physics.*;
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
public abstract class Entity implements TickObserver, Renderable, Collidable, Positionable {
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

    public void onCollision(Collidable entity) {}

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
            entity.addCollisionBox(new CollisionBox(entity, (ParserBlock) box));
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
