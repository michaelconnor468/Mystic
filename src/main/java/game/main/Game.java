package game.main;

import game.entities.Entity;
import util.parse.obj.*;
import util.parse.FileParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Represents abstract state of the game by listening to changes and setting the corresponding managers
 * accordingly. Made to abstract away game states and their transitions as much as possible from the various
 * managers.
 */
public class Game implements GameStateChangeListener {
    private X x;
    private Path loadFilePath;

    private Game() {}

    public Game(X x, double ticksPerSecond) {
        this.x = x;
        this.loadFilePath = Paths.get("src/main/config/worlds/default");
        x.getGameStateManager().addGameStateChangeListener(this);
        x.createRenderManager();
        load();
    }

    public void load() {
        x.createChunkManager(loadFilePath);
        x.createPlayer(loadFilePath);
    }
    
    public void load(Path loadFilePath) {
        x.createChunkManager(loadFilePath);
        x.createPlayer(loadFilePath);
    }

    public void beforeStateTransition(GameStateManager.State from, GameStateManager.State to) {
        switch ( from ) {
            case Playing:
                x.getTimingManager().stopTiming();
                x.getRenderManager().stop();
        }
    }
    public void afterStateTransition(GameStateManager.State from, GameStateManager.State to) {
        switch ( to ) {
            case Loading:
                Task<Void> task = new Task<Void>() {
                    @Override protected Void call() throws Exception {
                        x.createPlayer(loadFilePath);
                        x.getChunkManager().loadChunks(loadFilePath);
                        return null;
                    }
                };
                task.setOnSucceeded((v) -> x.getGameStateManager().setState(GameStateManager.State.Playing));
                (new Thread(task)).start();
                break;
            case Playing:
                x.getTimingManager().startTiming();
                x.getRenderManager().start();
                break;
        }
    }

    public void save(Path path) {

    }
}
