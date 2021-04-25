package views;

import views.scenes.MainMenuScene;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

// TODO remove when finished using
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.Slider;

/**
 * View for the main menu of the game which is the first thing to appear on startup. Allows user to edit configurations
 * and start the main game.
 */
public class MainMenuView implements View {
    int width;
    int height;

    private MainMenuView() {}
    public MainMenuView(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Scene deploy() { return MainMenuScene.getScene(width, height); }
    public void recall() {}
}
