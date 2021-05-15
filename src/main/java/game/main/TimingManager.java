package game.main;

import util.parse.obj.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Made solely to handle managing and dispatching to synchronizing ticks to the timed objects of the program. The decision of which objects these will be dispatched to
 * is left to the implementation as it is not the function of this class to know anything about which objects it is timing.
 */
public class TimingManager {
    private int ticksPerSecond;
    private ArrayList<TickObserver> tickObservers;
    private Timer timer;
    private X x;

    private TimingManager() {}

    public TimingManager(X x) {
        this.tickObservers = new ArrayList<>();
        this.timer = new Timer();
        this.ticksPerSecond = ((ParserInt) x.getMainSettings().getProperties().get("ticksPerSecond")).getNumber();
        this.x = x;
    }

    public void startTiming() {
        timer.schedule(new TimerTask()
            { public void run() { TimingManager.this.tick(x); } }, 
            (long) (1000/ticksPerSecond), 
            (long) (1000/ticksPerSecond));
    }

    public void stopTiming() {
        timer.cancel();
    }

    public void register(TickObserver o) {
        if ( !tickObservers.contains(o) )
            tickObservers.add(o);
    }

    public void unregister(TickObserver o) {
        tickObservers.remove(o);
    }

    private void tick(X x) {
        tickObservers.forEach(obs -> obs.tick(x));
    }

    public int getTicksPerSecond() { return ticksPerSecond; }
}
