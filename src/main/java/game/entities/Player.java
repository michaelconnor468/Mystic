package game.entities;

import util.parse.*;
import util.parse.obj.*;
import java.util.HashMap;
import game.main.render.Renderer;
import game.main.X;

public class Player extends DynamicEntity implements DestructibleEntity, Saveable {
    int health;
    int maxHealth;
 
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

    public ParserBlock save() {
        ParserBlock block = new ParserBlock();
        block.addProperty((new ParserProperty("maxHealth", new ParserInt(maxHealth))));
        block.addProperty((new ParserProperty("health", new ParserInt(health))));
        return block;
    }
}
