package views;

import views.scenes.MainMenuScene;
import game.main.X;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * View for the main menu of the game which is the first thing to appear on startup. Allows user to edit configurations
 * and start the main game.
 */
public class MainMenuView implements View {
    private int width;
    private int height;
    private X x;

    private MainMenuView() {}
    public MainMenuView(X x, int width, int height) {
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public Scene deploy() { return MainMenuScene.getScene(x, width, height); }
    public void recall() {}
}
