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
    private Weapon weapon;
    private Inventory inventory;
    private Player() {}

    public Player(X x, ParserBlock block, ParserBlock template) {
        super(x, block, template);
        HashMap<String, ParserObject> map = block.getProperties();
        ParserBlock weaponBlock = ((ParserBlock) map.get("weapon"));
        if ( weaponBlock.getProperties().containsKey("melee") )
            this.weapon = MeleeWeapon.load(x, this, weaponBlock);
        this.inventory = new Inventory(x, ((ParserBlock) map.get("inventory")));
        x.getTimingManager().register(this);
    }

    public void tick(X x) { 
        super.tick(x);
        System.out.println(position);
        animation.tick(x); 
    }
    
    public void render(Renderer r) { r.render(animation); }

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
    
    public void setMovementDirection(MovementDirection direction) {
        super.setMovementDirection(direction);
        animation.setStill(false);
        switch ( direction ) {
            case north:
                animation = walkNorthAnimation; 
                break;
            case northwest:
                animation = walkNorthWestAnimation;
                break;
            case northeast:
                animation = walkNorthEastAnimation;
                break;
            case south:
                animation = walkSouthAnimation;
                break;
            case southwest:
                animation = walkSouthWestAnimation;
                break;
            case southeast:
                animation = walkSouthEastAnimation;
                break;
            case east:
                animation = walkEastAnimation;
                break;
            case west:
                animation = walkWestAnimation;
                break;
        }
    }

    @Override public ParserBlock save(ParserBlock block) {
        super.save(block);
        this.weapon.save(block);
        this.inventory.save(block);
        return block;
    }

    @Override public void setStationary(boolean stationary) {
        animation.setStill(stationary);
        this.stationary = stationary;
    }

    public Inventory getInventory() { return this.inventory; }
    public void destroy() { x.getTimingManager().unregister(this); }
}
