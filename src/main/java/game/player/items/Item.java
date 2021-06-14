package game.player.items;

import util.parse.obj.*;
import game.main.X;
import game.player.Player;
import game.entities.StaticEntity;
import game.entities.Entity;
import game.main.render.Animation;
import game.main.render.Renderer;

import java.util.HashMap;
import java.util.Random;
import java.nio.file.Paths;
import java.awt.Point;

public class Item extends StaticEntity {
    private int id;
    private boolean inInventory;

    public static void drop(X x, int id, Entity entity) {
        Item item = new Item();
        HashMap<String, ParserObject> props = x.getTemplates("items").get(id).getProperties();
        item.x = x;
        item.name = ((ParserString) props.get("name")).toString();
        item.id = id;
        item.size = new Point(((ParserInt) x.getMainSettings().get("itemSize")).getNumber(),
            ((ParserInt) x.getMainSettings().get("itemSize")).getNumber());
        item.position = new Point((int) entity.getPosition().getX()+(new Random()).nextInt((int) entity.getSize().getX()),
                (int) entity.getPosition().getY() + (int) entity.getSize().getY() + (new Random()).nextInt(30));
        item.addCollisionBox(0, (int) item.size.getX(), 0, (int) item.size.getY());
        item.passable = true;
        item.inInventory = false;
        item.animation = new Animation(x, item, Paths.get("src/main/resources/items/"+item.id+".png"));
        x.getChunkManager().addEntity(item);
    }

    public void drop() {
        inInventory = false;
        position = new Point((int) x.getPlayer().getPosition().getX() + ((int) x.getPlayer().getSize().getX()/2),
            (int) (x.getPlayer().getPosition().getY() + x.getPlayer().getSize().getY()));
        x.getChunkManager().addEntity(this);
    }

    public void collected() { inInventory = true; }

    @Override public void onCollision(Entity entity) { if ( entity instanceof Player ) ((Player) entity).addItem(this); }
    @Override public void render(Renderer renderer) { if ( !inInventory ) renderer.render(animation); }
}
