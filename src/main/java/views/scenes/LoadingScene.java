package views.scenes;

import game.main.X;
import util.parse.obj.ParserInt;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class LoadingScene {
    public static Scene getScene(X x) {
        BorderPane borderPane = new BorderPane();
        int width = ((ParserInt) x.getMainSettings().get("resolutionx")).getNumber();
        int height = ((ParserInt) x.getMainSettings().get("resolutiony")).getNumber();
        Scene scene = new Scene(borderPane, width, height);
        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Common.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } 
        Text loading = new Text("Loading...");
        borderPane.setCenter(loading);

        return scene;
    }
}
