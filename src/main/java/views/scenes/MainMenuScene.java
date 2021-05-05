package views.scenes;

import game.main.X;
import game.main.GameStateManager;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class MainMenuScene {
    private static X x;

    public static Scene getScene(X context, int width, int height) {
        x = context;
        BorderPane borderPane = new BorderPane();

        Scene scene = new Scene(borderPane, width, height);
        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/MainMenu.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } // Should never happen but will print if it nevertheless does
        setTitle(borderPane);
        setCenterButtons(borderPane);
        return scene;
    }

    private static void setTitle(BorderPane borderPane) {
        Text title = new Text("Mystic");
        title.setFill(Color.rgb(10, 143, 48));
        title.getStyleClass().add("title");
        borderPane.setTop(title);
        borderPane.setAlignment(title, Pos.CENTER);
    }

    private static void setCenterButtons(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.prefWidthProperty().bind(borderPane.widthProperty());
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("button-box");
        
        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");
        // TODO loadGame functionality
        // TODO settings functionality
        newGameButton.setOnAction( e -> x.getGameStateManager().setState(GameStateManager.State.Loading) );
        exitButton.setOnAction( e -> {try{x.getApplication().stop();} catch (Exception err) {System.exit(0);}} );

        vbox.getChildren().add(newGameButton);
        vbox.getChildren().add(loadGameButton);
        vbox.getChildren().add(settingsButton);
        vbox.getChildren().add(exitButton);
        borderPane.setCenter(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
    }
}
