package game.main;

import game.entities.Entity;
/**
 * Class represents the context of a game which holds global variables. Since the same one will be passed through the tree of all classes, care should be taken
 * whenever modifiying any variables and many should remain read-only.
 */
public class X {
    public Game game;
    public int ticksPerSecond;
    public int tileSize;
    public int chunkSize;
    private WindowManager windowManager;

    public X() {
        windowManager = new WindowManager();
    }

    public Game getGame() {return game;}
    public void setGame(Game game) {this.game = game;}

    public void addEntity(Entity entity) {
        game.addEntity(entity);
    }

    public WindowManager getWidnowManager() {return windowManager;}
}
