package game.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WindowManager {
    Stage stage;

    private WindowManager() {}
    public WindowManager(Stage stage) {
        this.stage = stage; 
        
        StackPane mainPane = new StackPane();
        Scene scene = new Scene(mainPane, 500, 300);
        stage.setScene(scene);
        stage.show();
    }
}
