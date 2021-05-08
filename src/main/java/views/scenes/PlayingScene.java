package views.scenes;

import game.main.X;
import util.parse.obj.*;

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
        Text temp = new Text("TODO implement playing view");
        tempPane.setTop(temp);
        tempPane.setAlignment(temp, javafx.geometry.Pos.CENTER);
        return new Scene(tempPane, width, height);
    }
}
