package views.scenes;

import game.main.X;
import game.entities.Player;
import game.entities.DynamicEntity;
import util.parse.obj.*;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.canvas.*;

public class PlayingScene {
    public static X x;

    public static Scene getScene(X context) {
        x = context;
        int width = ((ParserInt) x.getMainSettings().getProperties().get("resolutionx")).getNumber();
        int height = ((ParserInt) x.getMainSettings().getProperties().get("resolutiony")).getNumber();
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        Scene scene = new Scene(root, width, height);
        root.getChildren().add(canvas);
        x.getRenderManager().updateGraphicsContext(canvas.getGraphicsContext2D());
        setupKeystrokes(scene);

        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Common.css")
                .toUri().toURL().toExternalForm());
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Playing.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } 

        return scene; 
    }

    private static void setupKeystrokes(Scene scene) {
        scene.setOnKeyPressed( e -> {
            switch ( e.getCode() ) {
                case W:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.west )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.east )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northeast);
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.still )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.north);
                    break;
                case S:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.west )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.east )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southeast);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.still )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.south);
                    break;
                case A:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.north )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.south )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.still )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.west);
                    break;
                case D:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.north )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northeast);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.south )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southeast);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.still )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.east);
                    break;
            }
        });
        scene.setOnKeyReleased( e -> {
            switch ( e.getCode() ) {
                case W:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.northwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.west);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.northeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.east);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.north )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.still);
                    break;
                case S:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.west);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.east);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.south )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.still);
                    break;
                case A:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.northwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.north);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.south);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.west )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.still);
                    break;
                case D:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.northeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.north);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.south);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.east )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.still);
                    break;
            }
        });
    }
}
