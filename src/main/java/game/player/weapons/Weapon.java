package game.player.weapons;

import game.main.X;
import game.player.Player;
import util.parse.obj.*;

import java.util.HashMap;

public class Weapon {
    protected X x;
    protected Player player;
    protected String name;
    protected double physicalDamage;
    protected int durability;
    protected int range, speed; // distance to damage away from edge of player and ticks to do damage for

    protected Weapon() {}
    public static Weapon load(X x, Player player, ParserBlock block, Weapon weapon) {
        weapon.x = x;
        weapon.player = player;
        HashMap<String, ParserObject> props = block.getProperties();
        weapon.name = ((ParserString) props.get("name")).getString(); 
        weapon.physicalDamage = ((ParserDouble) props.get("physicalDamage")).getNumber(); 
        weapon.durability = ((ParserInt) props.get("durability")).getNumber();
        weapon.range = ((ParserInt) props.get("range")).getNumber();
        weapon.speed = (int) ((ParserDouble) props.get("speed")).getNumber()*
            ((ParserInt) x.getMainSettings().get("ticksPerSecond")).getNumber();
        return weapon;
    }

    public void use() {
        durability--; 
        if ( durability == 0 ) player.unequip();
    }

    public int getDurability() { return durability; }
    public Player getPlayer() { return player; }
    public int getRange() { return range; }
    public int getSpeed() { return speed; }
}
