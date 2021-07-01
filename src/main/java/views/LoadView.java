package views;

import views.scenes.LoadScene;
import game.main.X;

import javafx.scene.Scene;

/**
 * Allows user to transition to the main menu from a game or save a game to a file.
 */
public class LoadView implements View {
    private X x;

    private LoadView() {}
    public LoadView(X x) {
        this.x = x;
    }

    public Scene deploy() { return LoadScene.getScene(x); }
    public void recall() {}
}
