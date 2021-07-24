package views.scenes.components;

import game.main.X;
import game.player.Inventory;
import game.player.items.ItemStack;
import util.parse.obj.*;
import util.Observer;
import util.Observable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.MalformedURLException;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class InventoryComponent implements Component, Observer {
    private X x;
    private ArrayList<ImageView> backgrounds;
    private List<ItemStack> items;
    private HBox box;
    private int size;
    private int height;
    
    private InventoryComponent() {}
    public InventoryComponent(X x) {
        this.x = x;
        this.size = ((ParserInt) x.getMainSettings().get("inventorySlots")).getNumber();
        this.items = x.getPlayer().getInventory().getItemStacks();
        this.backgrounds = new ArrayList<>(Collections.nCopies(size, null));
        x.getPlayer().getInventory().addObserver(this);

        Path path = Paths.get("src/main/resources/misc/InventorySlot.png");
        try {
            for ( int i = 0; i < size; i++ )
                backgrounds.set(i, new ImageView(new Image(path.toUri().toURL().toString())));
        } catch ( MalformedURLException e ) { System.err.println("I should never happen."); }

        this.box = new HBox();
        render();
    }

    public void render() {
        box.getChildren().clear();
        box.setAlignment(Pos.CENTER);
        for ( int i = 0; i < size; i++ ) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(backgrounds.get(i));
            box.getChildren().add(stackPane);
            if ( items.get(i) != null ) {
                stackPane.getChildren().add(new ImageView(items.get(i).getImage()));
                BorderPane bp = new BorderPane();
                Text count = new Text(String.valueOf(items.get(i).getSize()));
                count.setFill(Color.WHITE);
                count.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
                bp.setBottom(count);
                bp.setMargin(count, new Insets(0,0,0,4));
                stackPane.getChildren().add(bp);
            }
        }
    }

    public Pane getComponent() {
        return box;
    }

    @Override public void update(Observable inventory) {
        if ( !(inventory instanceof Inventory) ) return;
        this.items = x.getPlayer().getInventory().getItemStacks();
        Platform.runLater(() -> render());
    }
}
