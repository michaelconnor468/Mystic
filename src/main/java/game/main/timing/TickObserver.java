package game.main.timing;

import game.main.X;
/**
 * Observer interface allows objects to by synchronised through only the barebone of means. This is meant to relegate any actions to the implementation
 * of such entities. Used as an interface primarily for polymorphic storage.
 */
public interface TickObserver {
    public void tick(X x);
}