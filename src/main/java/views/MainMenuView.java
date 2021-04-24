package views;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * View for the main menu of the game which is the first thing to appear on startup
 */
public class MainMenuView implements View {
    Scene scene;

    private MainMenuView() {}
    public MainMenuView(int width, int height) {
        StackPane stackPane = new StackPane();
        scene = new Scene(stackPane, width, height);
    }

    public Scene deploy() { return scene; }
    public void recall() {

    }
}
