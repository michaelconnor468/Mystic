package game.player.weapons;

import game.entities.StaticEntity;
import game.entities.Entity;
import game.player.Player;
import game.physics.*;
import game.main.render.Renderer;
import game.main.render.Animation;
import game.main.X;

import java.util.HashSet;
import java.nio.file.Paths;
import java.awt.Point;

/**
 * Uses collisions framework to deal damage to entities based on player state. This allows any animations and
 * related calculations to be independant of the player object and to be easily performed using the game engines
 * support for entity collision and animation.
 */
public class MeleeAttack extends StaticEntity {
    private int ticksToLive;
    private Player player;
    private int range;
    private int weaponId;
    private HashSet<Collidable> damaged;
    private Weapon weapon;
    private X x;

    private MeleeAttack() {}
    public MeleeAttack(X x, Player player) {
        this.weapon = player.getWeapon();
        this.damaged = new HashSet<>();
        this.ticksToLive = weapon.getSpeed();
        this.player = weapon.getPlayer();
        this.range = weapon.getRange();
        this.weaponId = weapon.getId();
        this.x = x;
        this.size = new Point(((int) player.getSize().getX()), (int) player.getSize().getY() + 2);
        this.position = new Point(((int) player.getPosition().getX()), (int) player.getPosition().getY());
        this.animation = new Animation(x, this, Paths.get("src/main/resources/weapons/melee/"+weaponId+".png"));
        createCollisionBoxes();
    }

    @Override public void render(Renderer renderer) { renderer.render(this.animation); }

    @Override public void tick(X x) {
        if ( ticksToLive == 0 ) x.getChunkManager().removeEntity(this);
        super.tick(x);
        x.getChunkManager().testCollision(this);
        removeCollisionBoxes();
        createCollisionBoxes();
        ticksToLive--;
    }

    private void createCollisionBoxes() {
        this.position = new Point( (int) player.getPosition().getX(), (int) player.getPosition().getY());
        addCollisionBox(new CollisionBox(this, new Point(-range, -range),
            new Point(((int) size.getX()) + range, ((int) size.getY()) + range), false));
    }

    @Override public void onCollision(Collidable entity) {
        if ( damaged.contains(entity) ) return;
            damaged.add(entity);
        // TODO make this check for damageable
        if ( entity instanceof Entity && entity != player ) ((Entity) entity).damage(weapon);
    }

}
