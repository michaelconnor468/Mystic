package game.main;

import game.entities.Entity;
/**
 * Class represents the context of a game which holds global variables. Since the same one will be passed through the tree of all classes, care should be taken
 * whenever modifiying any variables and many should remain read-only.
 */
public class X {
    private Game game;
    private int ticksPerSecond;
    private int tileSize;

    public Game getGame() {return game;}
    public void setGame(Game game) {this.game = game;}

    public void addEntity(Entity entity) {
        game.addEntity(entity);
    }
}