package views.scenes;

import game.main.X;
import game.player.Player;
import game.main.GameStateManager;
import game.entities.DynamicEntity;
import game.entities.buffs.Buff;
import util.parse.obj.*;
import views.scenes.components.*;

import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.canvas.*;
import javafx.geometry.Pos;

public class PlayingScene {
    public static X x;

    public static Scene getScene(X context) {
        x = context;
        int width = ((ParserInt) x.getMainSettings().get("resolutionx")).getNumber();
        int height = ((ParserInt) x.getMainSettings().get("resolutiony")).getNumber();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, width, height);
        Canvas canvas = new Canvas(width, height);

        BorderPane componentHolder = new BorderPane();
        InventoryComponent inventoryComponent = new InventoryComponent(x);
        componentHolder.setBottom(inventoryComponent.getComponent());
        componentHolder.setAlignment(inventoryComponent.getComponent(), Pos.CENTER);
        componentHolder.setMargin(inventoryComponent.getComponent(), new Insets(12,12,12,12));

        pane.getChildren().add(canvas);
        pane.getChildren().add(componentHolder);
        x.getRenderManager().updateGraphicsContext(canvas.getGraphicsContext2D());
        setupKeystrokes(scene);

        try { 
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Common.css")
                .toUri().toURL().toExternalForm());
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Playing.css")
                .toUri().toURL().toExternalForm());
            scene.getStylesheets().add(Paths.get("src/main/resources/styles/Components.css")
                .toUri().toURL().toExternalForm());
        } catch ( Exception e ) { System.err.println(e); } 

        return scene; 
    }

    private static void setupKeystrokes(Scene scene) {
        scene.setOnKeyPressed( e -> {
            switch ( e.getCode() ) {
                case W:
                    if ( x.getPlayer().isStationary() )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.north);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.west )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.east )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northeast);
                    x.getPlayer().setStationary(false);
                    break;
                case S:
                    if ( x.getPlayer().isStationary() )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.south);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.west )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.east )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southeast);
                    x.getPlayer().setStationary(false);
                    break;
                case A:
                    if ( x.getPlayer().isStationary() )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.west);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.north )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northwest);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.south )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southwest);
                    x.getPlayer().setStationary(false);
                    break;
                case D:
                    if ( x.getPlayer().isStationary() )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.east);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.north )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.northeast);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.south )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.southeast);
                    x.getPlayer().setStationary(false);
                    break;
                case SHIFT:
                    x.getPlayer().addBuff(Buff.load(x, x.getPlayer(), "running"));
                    break;
                case ESCAPE:
                    x.getGameStateManager().setState(GameStateManager.State.Paused);
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
                        x.getPlayer().setStationary(true);
                    break;
                case S:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.west);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.east);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.south )
                        x.getPlayer().setStationary(true);
                    break;
                case A:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.northwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.north);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southwest )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.south);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.west )
                        x.getPlayer().setStationary(true);
                    break;
                case D:
                    if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.northeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.north);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.southeast )
                        x.getPlayer().setMovementDirection(DynamicEntity.MovementDirection.south);
                    else if ( x.getPlayer().getMovementDirection() == DynamicEntity.MovementDirection.east )
                        x.getPlayer().setStationary(true);
                    break;
                case SHIFT:
                    x.getPlayer().removeBuff("running");
            }
        });
        scene.setOnMouseClicked( e -> { x.getPlayer().onClick(e); });
    }
}
