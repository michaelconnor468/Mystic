package views;

import views.scenes.SaveScene;
import game.main.X;

import javafx.scene.Scene;

/**
 * Allows user to transition to the main menu from a game or save a game to a file.
 */
public class SaveView implements View {
    private X x;

    private SaveView() {}
    public SaveView(X x) {
        this.x = x;
    }

    public Scene deploy() { return SaveScene.getScene(x); }
    public void recall() {}
}

