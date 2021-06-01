package game.player.weapons;

import game.entities.StaticEntity;
import game.player.Player;

/**
 * Uses collisions framework to deal damage to entities based on player state. This allows any animations and
 * related calculations to be independant of the player object and to be easily performed using the game engines
 * support for entity collision and animation.
 */
public class MeleeAttack extends StaticEntity {
    private MeleeAttack() {}
    public MeleeAttack(Player player) {

    }
}
