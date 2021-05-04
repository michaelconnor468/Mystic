package game.entities;

import util.parse.*;
import util.parse.obj.*;
import java.util.HashMap;
import game.main.render.Renderer;
import game.main.X;

/**
 * Main player class. Special entity as it will be used to dictate camera location and chunk loading. Will
 * be loaded separately from all other entities and will not belong to a particular class.
 */
public class Player extends DynamicEntity implements DestructibleEntity {
    private int health;
    private int maxHealth;
 
    private Player() {}

    public void tick(X x) { }

    public void render(Renderer renderer) {}

    public void damage( int health ) {
        this.health -= health;
        if ( this.health <= 0 )
            onDestroy();
    }

    public void heal( int health ) {
        this.health += health;
        if ( this.health > maxHealth )
            this.health = maxHealth;
    }

    public void onDestroy() {

    }

    public static Player load(X x, ParserBlock block) {
        Player player = new Player();
        HashMap<String, ParserObject> map = block.getProperties();
        player.xSize = ((ParserInt) map.get("xSize")).getNumber();
        player.ySize = ((ParserInt) map.get("ySize")).getNumber();
        player.xPosition = ((ParserInt) map.get("xPosition")).getNumber();
        player.yPosition = ((ParserInt) map.get("yPosition")).getNumber();
        player.maxHealth = ((ParserInt) map.get("maxHealth")).getNumber();
        player.health = ((ParserInt) map.get("health")).getNumber();
        return player;
    }

    public static ParserBlock save(Player player) {
        ParserBlock block = new ParserBlock();
        block.addProperty((new ParserProperty("maxHealth", new ParserInt(player.getMaxHealth()))));
        block.addProperty((new ParserProperty("health", new ParserInt(player.getHealth()))));
        return block;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
}
