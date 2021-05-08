package views;

import views.scenes.MainMenuScene;
import game.main.X;

import javafx.scene.Scene;

/**
 * View for the main menu of the game which is the first thing to appear on startup. Allows user to edit configurations
 * and start the main game.
 */
public class MainMenuView implements View {
    private X x;

    private MainMenuView() {}
    public MainMenuView(X x) {
        this.x = x;
    }

    public Scene deploy() { return MainMenuScene.getScene(x); }
    public void recall() {}
}
