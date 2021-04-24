package game.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WindowManager {
    public WindowManager() {
        Application.launch(App.class); 
    }

    public static class App extends Application {
        @Override
        public void start(Stage stage) {
            StackPane mainPane = new StackPane();
            Scene scene = new Scene(mainPane, 500, 300);
            stage.setScene(scene);
            stage.show();
        }
    }
}
