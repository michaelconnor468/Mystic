package game.main;

import game.entities.Entity;
/**
 * Main driver class of the game which is intended to hold meta information for interacing with the high level managers of the game that will relegate
 * all logic and render actions to their contained components (GUI Manager, Timing Manager, Loading Manager, e.x.). To acheive an extensible design, 
 * this class should not do anymore than provide a lightweight interface with the managers of the app to alert them of any major changes in game state 
 * i.e. starting, pausing, saving, and loading.
 */
public class Game {
    private TimingManager timingManager;
    private WindowManager windowManager;
    private X x;

    private Game() {
       
    }

    public Game(X x, double ticksPerSecond) {
        this();
        this.x = x;
        timingManager = new TimingManager(x, ticksPerSecond);
    }

    public void start() {
        timingManager.startTiming();
    }

    public void pause() {
        timingManager.stopTiming();
    }

    public X getX() {
        return x;
    }

    public void addEntity(Entity entity) {
        //TODO send to chunk manager
    }
}
