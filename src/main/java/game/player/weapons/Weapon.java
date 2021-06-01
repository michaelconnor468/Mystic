package game.player.weapons;

import game.main.X;
import game.player.Player;
import util.parse.obj.*;

import java.util.HashMap;

public class Weapon {
    private X x;
    private Player player;
    private String name;
    private double physicalDamage;
    private int durability;
    private int range;

    protected Weapon() {}
    public static Weapon load(X x, Player player, ParserBlock block, Weapon weapon) {
        weapon.x = x;
        weapon.player = player;
        HashMap<String, ParserObject> props = block.getProperties();
        weapon.name = ((ParserString) props.get("name")).getString(); 
        weapon.physicalDamage = ((ParserDouble) props.get("physicalDamage")).getNumber(); 
        weapon.durability = ((ParserInt) props.get("durability")).getNumber();
        weapon.range = ((ParserInt) props.get("range")).getNumber();
        return weapon;
    }

    public void use() {
        durability--; 
        if ( durability == 0 ) player.unequip();
    }

    public int getDurability() { return durability; }
}
