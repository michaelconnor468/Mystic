package game.player;

import java.util.HashMap;
import java.util.ArrayList;
import java.nio.file.Paths;

import game.main.render.Animation;
import game.main.render.Renderer;
import game.entities.DynamicEntity;
import game.player.weapons.Weapon;
import game.main.X;
import util.parse.*;
import util.parse.obj.*;

/**
 * Main player class. Special entity as it will be used to dictate camera location and chunk loading. Will
 * be loaded separately from all other entities and will not belong to a particular class.
 */
public class Player extends DynamicEntity {
    private Animation currentAnimation;
    private Animation walkNorthAnimation;
    private Animation walkNorthWestAnimation;
    private Animation walkNorthEastAnimation;
    private Animation walkSouthAnimation;
    private Animation walkSouthWestAnimation;
    private Animation walkSouthEastAnimation;
    private Animation walkWestAnimation;
    private Animation walkEastAnimation;

    private Weapon weapon;

    private Player() {
        super();
    }

    public void tick(X x) { 
        super.tick(x); 
        currentAnimation.tick(x); 
    }
    
    public void render(Renderer r) { r.render(currentAnimation); }

    public void equip(Weapon weapon) { if ( weapon.getDurability() > 0 ) this.weapon = weapon; }
    public void unequip() { this.weapon = null; }
    public Weapon getWeapon() { return this.weapon; }

    public void setMovementDirection(MovementDirection direction) {
        super.setMovementDirection(direction);
        currentAnimation.setStill(false);
        switch ( direction ) {
            case north:
                currentAnimation = walkNorthAnimation; 
                break;
            case northwest:
                currentAnimation = walkNorthWestAnimation;
                break;
            case northeast:
                currentAnimation = walkNorthEastAnimation;
                break;
            case south:
                currentAnimation = walkSouthAnimation;
                break;
            case southwest:
                currentAnimation = walkSouthWestAnimation;
                break;
            case southeast:
                currentAnimation = walkSouthEastAnimation;
                break;
            case east:
                currentAnimation = walkEastAnimation;
                break;
            case west:
                currentAnimation = walkWestAnimation;
                break;
            case still:
                currentAnimation.setStill(true);
                break;
        }
    }

    public static Player load(X x, ParserBlock block) {
        Player player = new Player();
        HashMap<String, ParserObject> map = block.getProperties();
        player.x = x;
        player.xSize = ((ParserInt) map.get("xSize")).getNumber();
        player.ySize = ((ParserInt) map.get("ySize")).getNumber();
        player.xPosition = ((ParserInt) map.get("xPosition")).getNumber();
        player.yPosition = ((ParserInt) map.get("yPosition")).getNumber();
        player.maxHealth = ((ParserInt) map.get("maxHealth")).getNumber();
        player.health = ((ParserInt) map.get("health")).getNumber();
        player.stamina = ((ParserInt) map.get("stamina")).getNumber();
        player.maxStamina = ((ParserInt) map.get("maxStamina")).getNumber();
        player.walkNorthAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_north.png"));
        player.walkNorthWestAnimation = 
            new Animation(x, player, Paths.get("src/main/resources/player/walk_northwest.png"));
        player.walkNorthEastAnimation = 
            new Animation(x, player, Paths.get("src/main/resources/player/walk_northeast.png"));
        player.walkSouthAnimation = 
            new Animation(x, player, Paths.get("src/main/resources/player/walk_south.png"));
        player.walkSouthWestAnimation = 
            new Animation(x, player, Paths.get("src/main/resources/player/walk_southwest.png"));
        player.walkSouthEastAnimation = 
            new Animation(x, player, Paths.get("src/main/resources/player/walk_southeast.png"));
        player.walkWestAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_west.png"));
        player.walkEastAnimation = new Animation(x, player, Paths.get("src/main/resources/player/walk_east.png"));
        player.currentAnimation = player.walkSouthEastAnimation;
        player.direction = MovementDirection.still;
        player.speed = ((ParserInt) map.get("speed")).getNumber();
        player.addCollisionBox((ParserBlock) map.get("collisionBox"));
        player.damageable = true;
        x.getTimingManager().register(player);
        return player;
    }

    public static ParserBlock save(Player player) {
        return null;
    }
}
