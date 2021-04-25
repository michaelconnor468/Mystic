package views.scenes;

import game.main.X;
import game.main.GameStateManager;

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
        borderPane.setStyle("-fx-background-color: #E0E0E0;");

        Scene scene = new Scene(borderPane, width, height);

        setTitle(borderPane);
        setCenterButtons(borderPane);

        return scene;
    }

    private static void setTitle(BorderPane borderPane) {
        Text title = new Text("Mystic");
        title.setFill(Color.rgb(10, 143, 48));
        title.setFont(Font.font("Serif", 128));
        borderPane.setTop(title);
        borderPane.setAlignment(title, Pos.CENTER);
    }

    private static void setCenterButtons(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.prefWidthProperty().bind(borderPane.widthProperty());
        vbox.setAlignment(Pos.CENTER);
        
        Button startButton = new Button("New Game");
        Button exitButton = new Button("Exit");
        startButton.setOnAction( e -> x.getGameStateManager().setState(GameStateManager.State.Loading) );
        exitButton.setOnAction( e -> {try{x.getApplication().stop();} catch (Exception err) {System.exit(0);}} );

        vbox.getChildren().add(startButton);
        vbox.getChildren().add(exitButton);
        borderPane.setCenter(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
    }
}
