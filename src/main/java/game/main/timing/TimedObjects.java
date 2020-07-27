package game.main.timing;

import java.util.ArrayList;
import java.util.Iterator;
import game.main.X;

/**
 * Holds objects which are synchronized with the timer in TimingManager. This class effectively decouples the dispatching of ticks to the game objects
 * from their generation and management. It is not the purpose of this class to store all objects being synchronized as was originally intended, instead
 * this is to be delegated by from the objects stored here to their logical components for further redoucing coupling within the program.
 */
public class TimedObjects implements TickObserver {
    private ArrayList<TickObserver> tickObservers;

    public TimedObjects() {
        tickObservers = new ArrayList<TickObserver>();
    }

    public void tick(X x) {
        Iterator<TickObserver> iterator = tickObservers.iterator();
        while ( iterator.hasNext() ) {
            ((TickObserver) iterator.next()).tick(x);
        }
    }

    public void add(TickObserver toAdd) {
        tickObservers.add(toAdd);
    }

    public void remove(TickObserver toRemove) {
        Iterator<TickObserver> iterator = tickObservers.iterator();
        while ( iterator.hasNext() ) {
            if ( iterator.next() == toRemove )
                iterator.remove();
        }
    }
}