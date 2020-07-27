package game.entities.buffs;

import game.main.timing.TickObserver;
import game.main.X;

/**
 * Allows for buffs to be rendered void and be removed after a set number of time. It is the buffs responsibility to decide when its effects should no longer be
 * applied to simplify BuffContainer logic.
 */
public abstract class TimedEntityBuff extends EntityBuff implements TickObserver {
    private int tickTimeout;

    public void tick() {
        tickTimeout--;
        if ( tickTimeout <= 0 )
            isActive = false;
    }
}