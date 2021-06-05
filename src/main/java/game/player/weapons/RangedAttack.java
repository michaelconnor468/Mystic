package game.player.weapons;

import game.player.Player;
import game.entities.DynamicEntity;

/**
 * Uses collisions framework to deal damage to entities based on player state. This allows any animations and
 * related calculations to be independant of the player object and to be easily performed using the game engines
 * support for entity collision and animation.
 */
public class RangedAttack extends DynamicEntity {
    private RangedAttack() {}
    public RangedAttack(Player player) {

    }
}
