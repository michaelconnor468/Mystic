package views.scenes;

import game.main.X;
import game.main.GameStateManager;
import util.parse.obj.ParserInt;

import java.nio.file.Paths;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
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
       
        ArrayList<Path> savePaths = new ArrayList<>(Collections.nCopies(3, null));
        Pattern pattern = Pattern.compile("^[0-2]{1}$");
        try ( Stream<Path> paths = Files.walk(Paths.get("src/main/saves"), 1) ) {
            paths.forEach(f -> {
                if ( f.toString().length() > 0 ) {
                    Matcher matcher = pattern.matcher(f.toString().substring(f.toString().length() - 1));
                    if ( matcher.find() )
                        savePaths.set(Integer.parseInt(f.toString().substring(f.toString().length() - 1)), f);
                }
            });
        } catch ( Exception e ) { 
            e.printStackTrace(new java.io.PrintStream(System.err));  
            System.exit(1);
        }

        Button backButton = new Button("Back");
        vbox.getChildren().add(backButton);
        backButton.setOnAction( e -> x.getGameStateManager().setState(GameStateManager.State.MainMenu) );

        for ( int i = 0; i < 2; i++ ) {
            Button button = new Button(savePaths.get(i) == null ? "Empty" : "Slot " + (i+1));
            final int j = i;
            button.setOnAction( e -> {
                if ( savePaths.get(j) == null ) return;
                x.getGame().setLoadFilePath(savePaths.get(j));
                x.getGameStateManager().setState(GameStateManager.State.Loading);
            });
            vbox.getChildren().add(button);
        }
        
        borderPane.setCenter(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
    }
}
