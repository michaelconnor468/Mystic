package views.scenes;

import game.main.X;
import game.main.GameStateManager;
import util.parse.obj.ParserInt;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

public class PausedScene {
    private static X x;

    public static Scene getScene(X context) {
        x = context;
        int width = ((ParserInt) context.getMainSettings().get("resolutionx")).getNumber();
        int height = ((ParserInt) context.getMainSettings().get("resolutiony")).getNumber();
        BorderPane borderPane = new BorderPane();

        Scene scene = new Scene(borderPane, width, height);
        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Common.css")
                .toUri().toURL().toExternalForm());
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/MainMenu.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } 

        Text paused = new Text("Paused");
        paused.getStyleClass().add("title");
        borderPane.setTop(paused);
        borderPane.setAlignment(paused, Pos.CENTER);

        setCenterButtons(borderPane);
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

    private static void setCenterButtons(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.prefWidthProperty().bind(borderPane.widthProperty());
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("button-box");

        Button backButton = new Button("Back");
        // TODO save functionality
        Button saveButton = new Button("Save");
        Button menuButton = new Button("Quit");

        backButton.setOnAction( e -> x.getGameStateManager().setState(GameStateManager.State.Playing) );
        menuButton.setOnAction( e -> x.getGameStateManager().setState(GameStateManager.State.MainMenu) );

        vbox.getChildren().add(backButton);
        vbox.getChildren().add(saveButton);
        vbox.getChildren().add(menuButton);

        borderPane.setCenter(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
    }
}
