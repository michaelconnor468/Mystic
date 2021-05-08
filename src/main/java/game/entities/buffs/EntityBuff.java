package game.entities.buffs;

import game.main.TickObserver;
import game.main.X;

/**
 * Represents a modifier on the properties of an entity. Allows for abstraction and easy modification of logical groups of such modifiers that can
 * be applied and removed from entities as desired.
 */
public abstract class EntityBuff implements TickObserver {
    protected boolean isActive;

    public void tick(X x) {}
    public boolean isActive() { return isActive; }
}