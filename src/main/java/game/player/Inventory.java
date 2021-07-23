package game.player;

import game.main.X;
import game.entities.Saveable;
import util.parse.obj.*;

import java.util.Observable;

public class Inventory extends Observable implements Saveable  {
    private X x;

    private Inventory() {}
    public Inventory(X x) {
        this.x = x;
    }

    @Override public ParserBlock save(ParserBlock block) {
        ParserArray parserArray = new ParserArray(ParserObject.ObjectType.BLOCK);
        // TODO save into array
        block.addProperty(new ParserProperty("inventory", parserArray));
        return block;
    }
}
