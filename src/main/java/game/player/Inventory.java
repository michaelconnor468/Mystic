package game.player;

import game.main.X;
import game.entities.Saveable;
import util.parse.obj.*;

import java.util.Observable;

public class Inventory extends Observable implements Saveable  {
    private X x;

    private Inventory() {}
    public Inventory(X x, ParserBlock inventory) {
        for ( ParserObject object : (ParserArray) inventory.getProperty("items") ) {
            ParserBlock item = (ParserBlock) object;
            // TODO unload
        }
        this.x = x;
    }

    @Override public ParserBlock save(ParserBlock block) {
        ParserBlock inventoryBlock = new ParserBlock();
        ParserArray parserArray = new ParserArray(ParserObject.ObjectType.BLOCK);
        // TODO save into array
        inventoryBlock.addProperty(new ParserProperty("items", parserArray));
        block.addProperty(new ParserProperty("inventory", inventoryBlock));
        return block;
    }
}
