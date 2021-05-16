package game.main;

import util.parse.obj.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Forms the backbone of the game's asynchronous runtime. On any given pulse of a timer, registered game components are
 * notified and given a context object in order to execute their logic. Components should generally normalize their
 * timing using the ticksPerSecond setting to acheive consistancy in case of timing changes. This is user over a
 * synchronous game loop to help with easier multithreading as ticks may be sent in separate threads for thread safe
 * components.
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
