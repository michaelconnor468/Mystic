package game.player.weapons;

import game.entities.StaticEntity;
import game.entities.Entity;
import game.player.Player;
import game.main.render.Renderer;
import game.main.X;

import java.util.HashSet;

/**
 * Uses collisions framework to deal damage to entities based on player state. This allows any animations and
 * related calculations to be independant of the player object and to be easily performed using the game engines
 * support for entity collision and animation.
 */
public class MeleeAttack extends StaticEntity {
    private int ticksToLive;
    private Player player;
    private int range;
    private HashSet<Entity> damaged;
    private Weapon weapon;

    private MeleeAttack() {}
    public MeleeAttack(Player player) {
        this.weapon = player.getWeapon();
        this.damaged = new HashSet<>();
        this.ticksToLive = weapon.getSpeed();
        this.player = weapon.getPlayer();
        this.range = weapon.getRange();
        createCollisionBoxes();
    }

    private void createCollisionBoxes() {

    }

    public void tick(X x) {
        if ( ticksToLive == 0 ) x.getChunkManager().removeEntity(this);
        super.tick(x);
        removeCollisionBoxes();
        createCollisionBoxes();
        ticksToLive--;
    }
}
