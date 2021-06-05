package game.player.weapons;

import game.main.X;
import game.player.Player;
import util.parse.obj.*;

import java.util.HashMap;

public class Weapon {
    protected X x;
    protected Player player;
    protected String name;
    protected int id;
    protected double physicalDamage;
    protected int durability;
    protected int maxDurability;
    protected int range, speed; // distance to damage away from edge of player and ticks to do damage for

    protected Weapon() {}
    public static Weapon load(X x, Player player, ParserBlock block, Weapon weapon) {
        weapon.x = x;
        weapon.player = player;
        HashMap<String, ParserObject> props = block.getProperties();
        weapon.id = ((ParserInt) props.get("id")).getNumber();
        HashMap<String, ParserObject> template = x.getTemplates("melee").get(weapon.id).getProperties();
        weapon.name = ((ParserString) template.get("name")).getString(); 
        weapon.physicalDamage = props.containsKey("physicalDamage") ? 
            ((ParserDouble) props.get("physicalDamage")).getNumber() : 
            ((ParserDouble) template.get("physicalDamage")).getNumber(); 
        weapon.durability = props.containsKey("durability") ? ((ParserInt) props.get("durability")).getNumber() :
            ((ParserInt) template.get("durability")).getNumber();
        weapon.maxDurability = props.containsKey("maxDurability") ? ((ParserInt) props.get("maxDurability")).getNumber() :
            ((ParserInt) template.get("maxDurability")).getNumber();
        weapon.range = props.containsKey("range") ? ((ParserInt) props.get("range")).getNumber() : 
            ((ParserInt) template.get("range")).getNumber();
        weapon.speed = (int) (props.containsKey("speed") ? ((ParserDouble) props.get("speed")).getNumber() : 
            ((ParserDouble) template.get("speed")).getNumber())*
            ((ParserInt) x.getMainSettings().get("ticksPerSecond")).getNumber();
        return weapon;
    }

    public void use() {
        durability--; 
        if ( durability == 0 ) player.unequip();
    }

    public int getDurability() { return durability; }
    public void addDurability( int durability ) {this.durability = Math.max(this.durability + durability,maxDurability);}
    public int getMaxDurability() { return maxDurability; }
    public int getId() { return id; }
    public double getPhysicalDamage() { return physicalDamage; }
    public Player getPlayer() { return player; }
    public int getRange() { return range; }
    public int getSpeed() { return speed; }
}
