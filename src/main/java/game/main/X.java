package game.main;

import game.entities.Entity;

import javafx.stage.Stage;
import javafx.application.Application;
/**
 * Class represents the context of a game which holds global variables. Since the same one will be passed through 
 * the tree of all classes, care should be taken whenever modifiying any variables and many should remain read-only.
 */
public class X {
    private Game game;
    private WindowManager windowManager;
    private GameStateManager gameStateManager;
    private Application application;
    public int ticksPerSecond;
    public int tileSize;
    public int chunkSize;

    public X() {
    }

    public void addEntity(Entity entity) {
        game.addEntity(entity);
    }

    public Game getGame() {return game;}
    public void createGameSingleton(double ticksPerSecond) {
        if ( game == null )
            game = new Game(this, ticksPerSecond);
    }
    public WindowManager getWindowManager() {return windowManager;}
    public void createWindowManagerSingleton(Stage stage) { 
        if ( windowManager == null ) 
            windowManager = new WindowManager(this, stage);
    }
    public GameStateManager getGameStateManager() {return gameStateManager;}
    public void createGameStateManagerSingleton() {
        if ( gameStateManager == null )
            gameStateManager = new GameStateManager(this);
    }
    public Application getApplication() { return application; }
    public void createApplicationSingleton(Application application) {
        if ( this.application != null )
            this.application = application;
    }
}
