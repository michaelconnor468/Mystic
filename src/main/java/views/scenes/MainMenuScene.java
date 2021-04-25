package views.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class MainMenuScene {
    public static Scene getScene(int width, int height) {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, width, height);
        return scene;
    }
}
