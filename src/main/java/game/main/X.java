package game.main;

import game.entities.Entity;
import javafx.stage.Stage;
/**
 * Class represents the context of a game which holds global variables. Since the same one will be passed through the tree of all classes, care should be taken
 * whenever modifiying any variables and many should remain read-only.
 */
public class X {
    private Game game;
    private WindowManager windowManager;
    private GameStateManager gameStateManager;
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
            windowManager = new WindowManager(stage);
    }
    public GameStateManager getGameState() {return gameStateManager;}
    public void createGameStateManagerSingleton() {
        if ( gameStateManager == null )
            gameStateManager = new GameStateManager();
    }
}
