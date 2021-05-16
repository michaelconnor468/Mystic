package game.main;

import game.main.X;
/**
 * Observer interface allows objects to by synchronised through barebone means. This is meant to relegate any actions 
 * to the implementation of other classes, leaving them free and flexible to register to be ticked and decide what to
 * do upon each tick. Used as an interface primarily for polymorphic storage.
 */
public interface TickObserver {
    public void tick(X x);
}
