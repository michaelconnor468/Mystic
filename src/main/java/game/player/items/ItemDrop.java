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
public class ItemDrop extends StaticEntity {
    private Item item;
    private ItemDrop() {}

    public ItemDrop(X x, ParserBlock block) {
        super(x, block, block);
        this.item = new Item(x, ((ParserInt) block.getProperty("type")).getNumber());
        this.animation = new Animation(x, this, Paths.get("src/main/resources/items/"+item.getId()+".png"));
        this.passable = true;
        this.saveable = false;
    }

    public ItemDrop(X x, int id, Entity entity) { 
        ParserBlock block = x.getTemplates("items").get(id);
        this.x = x;
        this.item = new Item(x, ((ParserInt) block.getProperty("type")).getNumber());
        HashMap<String, ParserObject> props = block.getProperties();
        this.size = new Point(((ParserInt) props.get("xSize")).getNumber(), 
            ((ParserInt) props.get("ySize")).getNumber());
        this.animation = new Animation(x, this, Paths.get("src/main/resources/items/"+item.getId()+".png"));
        this.passable = true;
        this.saveable = false;
        this.position = 
            new Point2D.Double((int) entity.getPosition().getX()+(new Random()).nextInt((int) entity.getSize().getX()),
            (int) entity.getPosition().getY() + (int) entity.getSize().getY() + (new Random()).nextInt(30));
        this.addCollisionBox(new CollisionBox(this, new Point(0, (int) this.size.getX()), 
            new Point(0, (int) this.size.getY()), false));
    }
    
    @Override public void onCollision(Collidable entity) {
        if ( entity instanceof Player ) {
            ((Player) entity).addItem(item);
            x.getChunkManager().removeEntity(this);
        }
    }

    @Override public void render(Renderer renderer) { renderer.render(animation); }
}
