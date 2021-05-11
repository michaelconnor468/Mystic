package game.entities;

import util.parse.*;
import util.parse.obj.*;
import java.util.HashMap;
import java.nio.file.Paths;

import game.main.render.Animation;
import game.main.render.Renderer;
import game.main.X;

/**
 * Main player class. Special entity as it will be used to dictate camera location and chunk loading. Will
 * be loaded separately from all other entities and will not belong to a particular class.
 */
public class Player extends DynamicEntity implements DestructibleEntity {
    private int health;
    private int maxHealth;

    private Animation currentAnimation;
    private Animation walkUpAnimation;
    private Animation walkDownAnimation;
    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;
 
    private Player() {}

    public void tick(X x) { 
        currentAnimation.tick(x); 
    }

    public void render(Renderer r) { r.render(currentAnimation); }

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
        player.walkUpAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_up.png"));
        player.walkDownAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_down.png"));
        player.walkLeftAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_left.png"));
        player.walkRightAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_right.png"));
        player.currentAnimation = player.walkUpAnimation;
        x.getTimingManager().register(player);
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
