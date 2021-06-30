package views;

import views.scenes.PausedScene;
import game.main.X;

import javafx.scene.Scene;

/**
 * Allows user to transition to the main menu from a game or save a game to a file.
 */
public class PausedView {
    private X x;

    private PausedView() {}
    public PausedView(X x) {
        this.x = x;
    }

    public Scene deploy() { return PausedScene.getScene(x); }
    public void recall() {}
}
