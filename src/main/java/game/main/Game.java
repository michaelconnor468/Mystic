package game.main;

import game.entities.Entity;
import util.parse.obj.*;
import util.parse.FileParser;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Comparator;
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
        x.createChunkManager();
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

    public void setLoadFilePath(Path path) { this.loadFilePath = path; }

    public void save(Path path) throws IOException {
        Files.createDirectories(path.resolve(Paths.get("player")));
        Files.createDirectories(path.resolve(Paths.get("chunks")));
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
            Files.createDirectories(path.resolve(Paths.get("player")));
            Files.createDirectories(path.resolve(Paths.get("chunks")));
            Files.write(path.resolve(Paths.get("player/player.msv")), 
                x.getPlayer().save(new ParserBlock()).toString().getBytes());
            x.getChunkManager().saveChunks(path.resolve(Paths.get("chunks")));
            x.getGameStateManager().setState(GameStateManager.State.MainMenu);
        } catch(Exception e) { 
            System.err.println("Unable to save game."); 
            e.printStackTrace(new java.io.PrintStream(System.err));
            throw new IOException(e);
        }
    }
}
