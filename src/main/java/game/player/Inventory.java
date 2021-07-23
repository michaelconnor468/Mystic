package game.player;

import game.main.X;
import game.player.items.Item;
import game.player.items.ItemStack;
import game.player.items.ItemDrop;
import game.entities.Saveable;
import util.parse.obj.*;

import java.util.Observable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Inventory extends Observable implements Saveable  {
    private X x;
    private int slots;
    private int stackSize;
    private ArrayList<ItemStack> items;

    private Inventory() {}
    public Inventory(X x, ParserBlock inventory) {
        this.x = x;
        this.slots = ((ParserInt) x.getMainSettings().get("inventorySlots")).getNumber();
        this.items = new ArrayList<>(Collections.nCopies(slots, null));
        for ( ParserObject object : (ParserArray) inventory.getProperty("items") )
            items.add(new ItemStack(x, (ParserBlock) object));
    }

    public boolean addItem(Item item) {
        for ( ItemStack stack : items ) {
            if ( item.getId() == stack.getId() && !stack.isFull() ) {
               stack.addItem();
               notifyObservers();
               return true;
            }
        }
        for ( int i = 0; i < items.size(); i++ ) {
            if ( items.get(i) == null ) {
                items.set(i, new ItemStack(x, item));
                notifyObservers();
                return true;
            }
        }
        return false;
    }

    @Override public ParserBlock save(ParserBlock block) {
        ParserBlock inventoryBlock = new ParserBlock();
        ParserArray parserArray = new ParserArray(ParserObject.ObjectType.BLOCK);
        // TODO save into array
        inventoryBlock.addProperty(new ParserProperty("items", parserArray));
        block.addProperty(new ParserProperty("inventory", inventoryBlock));
        return block;
    }

    public List<ItemStack> getItemStacks() { return this.items; }
}
