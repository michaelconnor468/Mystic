package game.main;

import java.util.Timer;
import java.util.TimerTask;
import game.main.timing.*;

/**
 * Made solely to handle managing and dispatching to synchronizing ticks to the timed objects of the program. The decision of which objects these will be dispatched to
 * is left to the implementation as it is not the function of this class to know anything about which objects it is timing.
 */
public class TimingManager {
    private double ticksPerSecond;
    private TimedObjects timedObjects;
    private Timer timer;
    private X x;

    private TimingManager() {
        this.timedObjects = new TimedObjects();
        this.timer = new Timer();
    }

    public TimingManager(X x, double ticksPerSecond) {
        this();
        this.ticksPerSecond = ticksPerSecond;
        this.x = x;
    }

    public void startTiming() {
        timer.schedule(new TimerTask(){public void run(){TimingManager.this.tick(x);}}, (long)(1000/ticksPerSecond), (long)(1000/ticksPerSecond));
    }

    public void stopTiming() {
        timer.cancel();
    }

    public void addTimedObject(TickObserver o) {
        timedObjects.add(o);
    }

    public void removeTimedObject(TickObserver o) {
        timedObjects.add(o);
    }

    private void tick(X x) {
        timedObjects.tick(x);
    }
}