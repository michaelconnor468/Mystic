package game.entities;

import util.parse.*;
import util.parse.obj.*;
import java.util.HashMap;
import game.main.X;

public class Player extends DynamicEntity implements DestructibleEntity, Saveable {
    int health;
    int maxHealth;
 
    public Player(X x, int xSize, int ySize, int xPosition, int yPosition) { super(x, xSize, ySize, xPosition, yPosition); }

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

    public static Player load( ParserBlock block ) {
        HashMap<String, ParserObject> map = block.getProperties();
        this.maxHealth = ((ParserInt) map.get(maxHealth)).getNumber();
        this.health = ((ParserInt) map.get(health)).getNumber();
    }

    public ParserBlock save() {
        ParserBlock block = new ParserBlock();
        block.addProperty((new ParserProperty("maxHealth", new ParserInt(maxHealth))));
        block.addProperty((new ParserProperty("health", new ParserInt(health))));
        return block;
    }
}
