package game.main;

import game.entities.Entity;
import game.entities.Player;
import util.parse.FileParser;
import util.parse.obj.ParserBlock;

import java.util.HashMap;
import java.nio.file.Paths;
import java.nio.file.Path;

import javafx.stage.Stage;
import javafx.application.Application;

/**
 * Class represents the context of a game which holds global variables and services. Since the same one will be passed 
 * through the tree of all classes, care should be taken whenever modifiying any variables and many should 
 * remain read-only.
 */
public class X {
    private Game game;
    private WindowManager windowManager;
    private GameStateManager gameStateManager;
    private ChunkManager chunkManager;
    private Application application;
    private ParserBlock mainSettings;
    private RenderManager renderManager;
    private TimingManager timingManager;
    private ParserBlock buffs;
    private Player player;

    public X() {}

    public Game getGame() { return game; }
    public void createGameSingleton(double ticksPerSecond) {
        if ( game == null )
            game = new Game(this, ticksPerSecond);
    }
    public WindowManager getWindowManager() { return windowManager; }
    public void createWindowManagerSingleton(Stage stage) { 
        if ( windowManager == null ) 
            windowManager = new WindowManager(this, stage);
    }
    public GameStateManager getGameStateManager() { return gameStateManager; }
    public void createGameStateManagerSingleton() {
        if ( gameStateManager == null )
            gameStateManager = new GameStateManager(this);
    }
    public Application getApplication() { return application; }
    public void createApplicationSingleton(Application application) {
        if ( this.application == null )
            this.application = application;
    }
    public ChunkManager getChunkManager() { return chunkManager; }
    public void createChunkManager(Path path) { 
        chunkManager = new ChunkManager(this, FileParser.parse(path.resolve(Paths.get("config/world.mcfg")))); 
    }
    public void createSettingsSingletons(Path path) {
        if ( mainSettings == null )
            mainSettings = FileParser.parse(path.resolve(Paths.get("main.mcfg")));
        if ( buffs == null )
            buffs = FileParser.parse(path.resolve(Paths.get("buffs.mcfg")));
    }
    public ParserBlock getMainSettings() { return mainSettings; }
    public void createPlayer(Path path) { 
        player = Player.load(this, FileParser.parse(path.resolve("entities/player.msv"))); 
    }
    public ParserBlock getBuffSettings() { return buffs; }
    public Player getPlayer() { return player; }
    public void createRenderManager() { renderManager = new RenderManager(this); }
    public RenderManager getRenderManager() { return renderManager; };
    public void createTimingManager() { timingManager = new TimingManager(this); }
    public TimingManager getTimingManager() { return timingManager; }
}
