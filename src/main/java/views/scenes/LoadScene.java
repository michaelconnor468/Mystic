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

public class LoadScene {
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

        Text save = new Text("Load");
        save.getStyleClass().add("title");
        borderPane.setTop(save);
        borderPane.setAlignment(save, Pos.CENTER);

        setCenterButtons(borderPane);

        return scene;
    }
    
    private static void setCenterButtons(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.prefWidthProperty().bind(borderPane.widthProperty());
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("button-box");

        Button backButton = new Button("Back");
        Button save1Button = new Button("Empty");
        Button save2Button = new Button("Empty");
        Button save3Button = new Button("Empty");

        backButton.setOnAction( e -> x.getGameStateManager().setState(GameStateManager.State.MainMenu) );

        vbox.getChildren().add(backButton);
        vbox.getChildren().add(save1Button);
        vbox.getChildren().add(save2Button);
        vbox.getChildren().add(save3Button);

        borderPane.setCenter(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
    }
}
