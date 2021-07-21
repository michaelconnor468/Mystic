package views.scenes.components;

import game.main.X;
import util.parse.obj.*;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InventoryComponent implements Component {
    private ArrayList<ImageView> items;
    private HBox box;
    private int size;
    private int height;
    
    private InventoryComponent() {}
    public InventoryComponent(X x) {
        this.size = ((ParserInt) x.getMainSettings().get("inventorySize")).getNumber();
        this.height = ((ParserInt) x.getMainSettings().get("inventoryHeight")).getNumber();
        this.items = new ArrayList<>();
        this.box = new HBox();
    }

    public Pane getComponent() {
        return box;
    }
}
