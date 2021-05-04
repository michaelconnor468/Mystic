package game.main;

import game.entities.Entity;
import util.parse.obj.*;
import util.parse.FileParser;

import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Main driver class of the game which is intended to hold meta information for interacing with the high level managers of the game that will relegate
 * all logic and render actions to their contained components (GUI Manager, Timing Manager, Loading Manager, e.x.). To acheive an extensible design, 
 * this class should not do anymore than provide a lightweight interface with the managers of the app to alert them of any major changes in game state 
 * i.e. starting, pausing, saving, and loading.
 */
public class Game implements GameStateChangeListener {
    private TimingManager timingManager;
    private WindowManager windowManager;
    private X x;
    private Path loadFilePath;

    private Game() {
       
    }

    public Game(X x, double ticksPerSecond) {
        this();
        this.x = x;
        timingManager = new TimingManager(x, ticksPerSecond);
        loadFilePath = Paths.get("src/main/config/worlds/default");
        x.createChunkManagerSingleton(FileParser.parse(loadFilePath.resolve(Paths.get("config/world.mcfg"))));
        x.getGameStateManager().addGameStateChangeListener(this);
    }

    public void start() {
        timingManager.startTiming();
    }

    public void pause() {
        timingManager.stopTiming();
    }

    public void beforeStateTransition(GameStateManager.State from, GameStateManager.State to){

    }

    public void afterStateTransition(GameStateManager.State from, GameStateManager.State to) {
        switch ( to ) {
            case Loading:
                x.getChunkManager().loadChunks(loadFilePath);
                break;
        }
    }
}
