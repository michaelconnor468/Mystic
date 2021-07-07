package game.player.items;

import util.parse.obj.*;
import game.main.X;
import game.player.Player;
import game.physics.*;
import game.entities.StaticEntity;
import game.entities.Entity;
import game.main.render.*;

import java.util.HashMap;
import java.util.Random;
import java.nio.file.Paths;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Entity wrapper allowing for items to be dropped without having item extend entity as it does not always behave
 * like one.
 */
public class ItemDrop extends Entity {
    private Item item;
    private ItemDrop() {}

    public ItemDrop(X x, ParserBlock block) {
        super(x, block, block);
        this.item = new Item(x, ((ParserInt) block.getProperty("id")).getNumber());
        this.animation = new Animation(x, this, Paths.get("src/main/resources/items/"+item.getId()+".png"));
        this.passable = true;
    }
}
