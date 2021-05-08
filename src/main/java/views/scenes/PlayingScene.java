package views.scenes;

import game.main.X;
import util.parse.obj.*;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class PlayingScene {
    public static X x;

    public static Scene getScene(X context) {
        x = context;
        int width = ((ParserInt) x.getMainSettings().getProperties().get("resolutionx")).getNumber();
        int height = ((ParserInt) x.getMainSettings().getProperties().get("resolutiony")).getNumber();
        BorderPane tempPane = new BorderPane();
        Scene scene = new Scene(tempPane, width, height);

        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Common.css")
                .toUri().toURL().toExternalForm());
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Playing.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } 

        Text temp = new Text("TODO implement playing view");
        tempPane.setTop(temp);
        tempPane.setAlignment(temp, javafx.geometry.Pos.CENTER);
        return scene; 
    }
}
