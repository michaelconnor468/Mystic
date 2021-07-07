package game.entities;

import game.main.TickObserver;
import game.main.render.*;
import game.main.X;
import game.entities.buffs.Buff;
import game.player.weapons.Weapon;
import game.player.items.ItemDrop;
import game.physics.*;
import util.parse.obj.*;
import util.parse.BlockParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Abstract class for entity within the game. To allow easy addition of future features, everything in the game is considered an entity allowing it to
 * be synchronized by the tick and render timers so that any logic and animation of arbitrary features may be easily added in the future without changing 
 * the front end drivers.
 */
public abstract class Entity implements TickObserver, Renderable, Collidable, Positionable {
    protected X x;
    protected String name;
    protected Point2D.Double position;
    protected Point size;
    protected int health;
    protected int maxHealth;
    protected boolean damageable = false;
    protected boolean passable = false;
    protected boolean saveable = true;
    protected ArrayList<CollisionBox> collisionBoxes = new ArrayList<>();
    protected ArrayList<Buff> buffs = new ArrayList<>(); 
    protected ArrayList<Integer> drops = new ArrayList<>(); 
    protected Animation animation;

    public Entity() {}
    protected Entity(X x, ParserBlock block, ParserBlock template) {
        this.x = x;
        HashMap<String, ParserObject> map = block.getProperties();
        HashMap<String, ParserObject> templateMap = template.getProperties();
        this.size = new Point(((ParserInt) loadProperty(block, template, "xSize")).getNumber(), 
            ((ParserInt) loadProperty(block, template, "ySize")).getNumber());
        this.position = new Point2D.Double(((ParserInt) map.get("xPosition")).getNumber(), 
            ((ParserInt) map.get("yPosition")).getNumber());
        this.maxHealth = ((ParserInt) loadProperty(block, template, "maxHealth")).getNumber();
        this.health = ((ParserInt) loadProperty(block, template, "health")).getNumber();
        if ( templateMap.containsKey("damageable") ) 
            this.damageable = ((ParserInt) loadProperty(block, template, "damageable")).getNumber() == 1;
        if ( templateMap.containsKey("passable") ) 
            this.passable = ((ParserInt) loadProperty(block, template, "passable")).getNumber() == 1;
        for ( ParserObject box : ((ParserArray) loadProperty(block, template, "collisionBoxes")) )
            addCollisionBox(new CollisionBox(this, (ParserBlock) box));
        ParserArray dropsArray = ((ParserArray) loadProperty(block, template, "drops"));
        if ( dropsArray != null )
            for ( ParserObject i : dropsArray )
                drops.add(((ParserInt) i).getNumber());
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
        for ( int drop : drops ) x.getChunkManager().addEntity(new ItemDrop(x, drop, this));
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

    public Point2D.Double getPosition() { return new Point2D.Double(position.getX(), position.getY()); }
    public Point getSize() { return new Point(size); } 
    public double getHealth() { return this.health; }
    public double getMaxHealth() { return this.maxHealth; }
    public String getName() { return this.name; }
    public ArrayList<CollisionBox> getCollisionBoxes() { return this.collisionBoxes; }

    public void onCollision(Collidable entity) {}

    public void save(ParserBlock block) {
        HashMap<String, ParserObject> map = block.getProperties();
        map.put("xSize", new ParserInt((int) size.getX()));
        map.put("ySize", new ParserInt((int) size.getY()));
        map.put("xPosition", new ParserInt((int) position.getX()));
        map.put("yPosition", new ParserInt((int) position.getY()));
        map.put("health", new ParserInt(health));
        map.put("maxHealth", new ParserInt(maxHealth));
        map.put("damageable", new ParserInt(damageable ? 1 : 0));
        map.put("passable", new ParserInt(passable ? 1 : 0));

        ParserArray collisionArray = new ParserArray(ParserObject.ObjectType.BLOCK);
        for ( CollisionBox box : getCollisionBoxes() ) {
            ParserBlock boxBlock = new ParserBlock();
            box.save(boxBlock);
            collisionArray.add(boxBlock);
        }
        map.put("collisionBoxes", collisionArray);

        ParserArray dropsArray = new ParserArray(ParserObject.ObjectType.INT);
        for ( Integer i : drops ) {
            dropsArray.add(new ParserInt(i));
        }
        map.put("drops", dropsArray);
    }

    public static ParserObject loadProperty(ParserBlock block, ParserBlock template, String name) {
        HashMap<String, ParserObject> templateProps = template.getProperties();
        HashMap<String, ParserObject> props = block.getProperties();
        return props.containsKey(name) ? props.get(name) : templateProps.get(name);
    }
}
