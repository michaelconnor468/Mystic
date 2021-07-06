package game.player;

import java.util.HashMap;
import java.util.ArrayList;
import java.nio.file.Paths;
import javafx.scene.input.MouseEvent;
import java.awt.Point;

import game.main.render.Animation;
import game.main.render.Renderer;
import game.entities.DynamicEntity;
import game.physics.*;
import game.player.weapons.*;
import game.player.items.*;
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

    public Player(X x, ParserBlock block, ParserBlock template) {
        super(x, block, template); // TODO refactor a lot of this to superclass constructors
        HashMap<String, ParserObject> map = block.getProperties();
        this.stamina = ((ParserInt) map.get("stamina")).getNumber();
        this.maxStamina = ((ParserInt) map.get("maxStamina")).getNumber();
        this.walkNorthAnimation = new Animation(x, this, Paths.get("src/main/resources/player/walk_north.png"));
        this.walkNorthWestAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/player/walk_northwest.png"));
        this.walkNorthEastAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/player/walk_northeast.png"));
        this.walkSouthAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/player/walk_south.png"));
        this.walkSouthWestAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/player/walk_southwest.png"));
        this.walkSouthEastAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/player/walk_southeast.png"));
        this.walkWestAnimation = new Animation(x, this, Paths.get("src/main/resources/player/walk_west.png"));
        this.walkEastAnimation = new Animation(x, this, Paths.get("src/main/resources/player/walk_east.png"));
        this.currentAnimation = this.walkSouthEastAnimation;
        this.direction = MovementDirection.west;
        this.stationary = true;
        this.speed = ((ParserInt) map.get("speed")).getNumber();
        ParserBlock weaponBlock = ((ParserBlock) map.get("weapon"));
        if ( weaponBlock.getProperties().containsKey("melee") )
            this.weapon = MeleeWeapon.load(x, this, weaponBlock);
        x.getTimingManager().register(this);
    }

    public void tick(X x) { 
        super.tick(x); 
        currentAnimation.tick(x); 
    }
    
    public void render(Renderer r) { r.render(currentAnimation); }

    public void onClick(MouseEvent e) {
        double mouseX = e.getX() - ((ParserInt) x.getMainSettings().get("resolutionx")).getNumber()/2;
        double mouseY = -(e.getY() - ((ParserInt) x.getMainSettings().get("resolutiony")).getNumber()/2);
        double angle = Math.toDegrees(Math.PI/2 - Math.atan(mouseY/mouseX)) + (mouseX > 0 ? 0 : 180);
        setDirectionAngle(angle);
        setStationary(true);
        if ( weapon != null ) weapon.use();
    }

    public void equip(Weapon weapon) { if ( weapon.getDurability() > 0 ) this.weapon = weapon; }
    public void unequip() { this.weapon = null; }
    public Weapon getWeapon() { return this.weapon; }
    
    public void addItem(Item item) { 
        item.collected();
    }

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
        }
    }

    public ParserBlock save() {
        return null;
    }

    @Override
    public void setStationary(boolean stationary) {
        currentAnimation.setStill(stationary);
        this.stationary = stationary;
    }

    public static ParserBlock save(Player player) {
        return null;
    }
}
