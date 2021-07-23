package views.scenes.components;

import game.main.X;
import game.player.Inventory;
import util.parse.obj.*;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.MalformedURLException;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;

public class InventoryComponent implements Component, Observer {
    private ArrayList<ImageView> items = new ArrayList<>();
    private ArrayList<ImageView> backgrounds = new ArrayList<>();
    private HBox box;
    private int size;
    private int height;
    
    private InventoryComponent() {}
    public InventoryComponent(X x) {
        this.size = ((ParserInt) x.getMainSettings().get("inventorySlots")).getNumber();
        this.items = new ArrayList<>();
        this.box = new HBox();

        try {
            for ( int i = 0; i < size; i++ ) {
                Path path = Paths.get("src/main/resources/misc/InventorySlot.png");
                ImageView imageView = new ImageView(new Image(path.toUri().toURL().toString()));
                backgrounds.add(imageView);
            }
        } catch ( MalformedURLException e ) { System.err.println("I can never happen."); }

        for ( int i = 0; i < size; i++ ) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(backgrounds.get(i));
            box.getChildren().add(stackPane);
        }

        box.setAlignment(Pos.CENTER);

    }

    public Pane getComponent() {
        return box;
    }

    @Override public void update(Observable inventory, Object arg) {
        if ( !(inventory instanceof Inventory) ) return;
        Inventory inv = (Inventory) inventory;
    }
}
