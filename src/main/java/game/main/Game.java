package game.main;

import game.entities.Entity;
import util.parse.obj.*;
import util.parse.FileParser;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Game implements GameStateChangeListener {
    private X x;
    private Path loadFilePath;

    private Game() {}

    public Game(X x, double ticksPerSecond) {
        this.x = x;
        this.loadFilePath = Paths.get("src/main/config/worlds/default");
        load();
        x.getGameStateManager().addGameStateChangeListener(this);
    }

    public void load() {
        x.createChunkManager(loadFilePath);
        x.createPlayer(loadFilePath);
        x.createTimingManager();
        x.createRenderManager();
    }

    public void beforeStateTransition(GameStateManager.State from, GameStateManager.State to) {}
    public void afterStateTransition(GameStateManager.State from, GameStateManager.State to) {
        switch ( to ) {
            case Loading:
                x.getChunkManager().loadChunks(loadFilePath);
            
                x.getGameStateManager().setState(GameStateManager.State.Playing);
                break;
        }
    }
}
