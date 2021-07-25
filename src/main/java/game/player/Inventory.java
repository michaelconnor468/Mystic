package game.player;

import game.main.X;
import game.player.items.Item;
import game.player.items.ItemStack;
import game.player.items.ItemDrop;
import game.entities.Saveable;
import util.parse.obj.*;
import util.Observable;
import util.Observer;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Inventory implements Saveable, Observable {
    private X x;
    private int slots;
    private int stackSize;
    private ArrayList<ItemStack> items;
    private ArrayList<Observer> observers;

    private Inventory() {}
    public Inventory(X x, ParserBlock inventory) {
        this.x = x;
        this.slots = ((ParserInt) x.getMainSettings().get("inventorySlots")).getNumber();
        this.items = new ArrayList<>(Collections.nCopies(slots, null));
        this.observers = new ArrayList<>();
        for ( ParserObject object : (ParserArray) inventory.getProperty("items") )
            items.add(new ItemStack(x, (ParserBlock) object));
    }

    public boolean addItem(Item item) {
        for ( ItemStack stack : items ) {
            if ( stack != null && item.getId() == stack.getId() && !stack.isFull() ) {
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
        for ( ItemStack item : items ) if ( item != null ) parserArray.add(item.save(new ParserBlock()));
        inventoryBlock.addProperty(new ParserProperty("items", parserArray));
        block.addProperty(new ParserProperty("inventory", inventoryBlock));
        return block;
    }

    public List<ItemStack> getItemStacks() { return this.items; }
    @Override public List<Observer> getObservers() { return observers; }
}
