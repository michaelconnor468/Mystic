package views.scenes.components;

import game.main.X;
import game.player.Inventory;
import game.player.items.ItemStack;
import util.parse.obj.*;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Collections;
import java.util.List;
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
    private X x;
    private ArrayList<ImageView> items;
    private ArrayList<Integer> itemCounts;
    private ArrayList<ImageView> backgrounds;
    private HBox box;
    private int size;
    private int height;
    
    private InventoryComponent() {}
    public InventoryComponent(X x) {
        this.x = x;
        this.size = ((ParserInt) x.getMainSettings().get("inventorySlots")).getNumber();
        this.items = new ArrayList<>(Collections.nCopies(size, null));
        this.itemCounts = new ArrayList<>(Collections.nCopies(size, null));
        this.backgrounds = new ArrayList<>(Collections.nCopies(size, null));
        x.getPlayer().getInventory().addObserver(this);

        Path path = Paths.get("src/main/resources/misc/InventorySlot.png");
        try {
            for ( int i = 0; i < size; i++ ) 
                backgrounds.set(i, new ImageView(new Image(path.toUri().toURL().toString())));
        } catch ( MalformedURLException e ) { System.err.println("I should never happen."); }
        

        List<ItemStack> inventory = x.getPlayer().getInventory().getItemStacks();
        for ( int i = 0; i < inventory.size(); i++ ) {
            if ( inventory.get(i) != null ) {
                items.set(i, new ImageView(inventory.get(i).getImage()));
                itemCounts.set(i, inventory.get(i).getSize());
            }
        }

        render();
    }

    public void render() {
        this.box = new HBox();
        box.setAlignment(Pos.CENTER);
        for ( int i = 0; i < size; i++ ) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(backgrounds.get(i));
            box.getChildren().add(stackPane);
            //if ( items.get(i) != null ) stackPane.getChildren().add(items.get(i));
        }
    }

    public Pane getComponent() {
        return box;
    }

    @Override public void update(Observable inventory, Object arg) {
        if ( !(inventory instanceof Inventory) ) return;
        Inventory inv = (Inventory) inventory;
    }
}
