package views.scenes;

import game.main.X;
import game.main.GameStateManager;
import util.parse.obj.ParserInt;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class PausedScene {
    private static X x;

    public static Scene getScene(X context) {
        BorderPane borderPane = new BorderPane();
        x = context;
        int width = ((ParserInt) context.getMainSettings().get("resolutionx")).getNumber();
        int height = ((ParserInt) context.getMainSettings().get("resolutiony")).getNumber();
        Scene scene = new Scene(borderPane, width, height);
        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Common.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } 

        Text paused = new Text("Paused");
        borderPane.setCenter(paused);

        setupKeystrokes(scene);

        return scene;
    }
    
    private static void setupKeystrokes(Scene scene) {
        scene.setOnKeyPressed( e -> {
            switch ( e.getCode() ) {
                case ESCAPE:
                    x.getGameStateManager().setState(GameStateManager.State.Playing);
                    break;
            }
        });
    }
}
