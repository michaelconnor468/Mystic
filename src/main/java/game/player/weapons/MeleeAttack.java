package game.player.weapons;

import game.entities.StaticEntity;
import game.entities.Entity;
import game.player.Player;
import game.main.render.Renderer;
import game.main.render.Animation;
import game.main.X;

import java.util.HashSet;
import java.nio.file.Paths;

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
    private HashSet<Entity> damaged;
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
        this.ySize = player.getySize();
        this.xSize = player.getxSize();
        this.xPosition = player.getxPosition();
        this.yPosition = player.getyPosition();
        createCollisionBoxes();
    }

    @Override public void render(Renderer renderer) {}

    @Override public void tick(X x) {
        if ( ticksToLive == 0 ) x.getChunkManager().removeEntity(this);
        super.tick(x);
        x.getChunkManager().isColliding(this);
        removeCollisionBoxes();
        createCollisionBoxes();
        ticksToLive--;
    }

    private void createCollisionBoxes() {
        this.xPosition = player.getxPosition();
        this.yPosition = player.getyPosition();
        addCollisionBox(-range, xSize + range, -range, ySize + range);
    }

    public void onCollision(Entity entity) {
        if ( damaged.contains(entity) ) return;
        damaged.add(entity);
        if ( entity != player ) entity.damage(weapon);
    }

}
