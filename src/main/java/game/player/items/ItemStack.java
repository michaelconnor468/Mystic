package game.player.items;

import game.main.X;
import game.entities.Saveable;
import util.parse.obj.*;

import java.nio.file.Paths;
import javafx.scene.image.Image;

public class ItemStack implements Saveable {
    private Item item;
    private X x;
    private int size;
    private int maxSize;
    private Image image;
    
    private ItemStack() {}
    public ItemStack(X x, Item item) { this(x, item, 1); }
    public ItemStack(X x, ParserBlock block) {
        this(x, new Item(x, ((ParserInt) block.getProperty("id")).getNumber()), 
            ((ParserInt) block.getProperty("size")).getNumber());
    }
    public ItemStack(X x, Item item, int size) {
        this.x = x;
        this.maxSize = ((ParserInt) x.getMainSettings().get("itemStackSize")).getNumber(); 
        this.size = 1;
        this.item = item;
        try {
            this.image = 
                new Image(Paths.get("src/main/resources/items/"+item.getId()+".png").toUri().toURL().toString());
        } catch ( Exception e ) { System.err.println(e.getMessage()); }
    }

    public Image getImage() { return image; }
    public int getSize() { return size; }
    public int getId() { return item.getId(); }
    public boolean isFull() { return size == maxSize; }
    public boolean isEmpty() { return size == 0; }
    public void addItem() { size = size == maxSize ? size : size + 1; }
    public void removeItem() { size = size == 0 ? 0 : size + 1; }

    @Override public ParserBlock save(ParserBlock block) {
        block.addProperty(new ParserProperty("id", new ParserInt(item.getId())));
        block.addProperty(new ParserProperty("size", new ParserInt(size)));
        return block;
    }

    @Override public String toString() { return "{" + "item: " + item.getId() + ", size: " + size + "}"; }
}
