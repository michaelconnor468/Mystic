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

public class Item extends StaticEntity {
    private int id;
    private boolean inInventory;

    public static void drop(X x, int id, Entity entity) {
        Item item = new Item();
        HashMap<String, ParserObject> props = x.getTemplates("items").get(id).getProperties();
        item.x = x;
        item.name = ((ParserString) props.get("name")).toString();
        item.id = id;
        item.xSize = ((ParserInt) x.getMainSettings().get("itemSize")).getNumber();
        item.ySize = ((ParserInt) x.getMainSettings().get("itemSize")).getNumber();
        item.xPosition = entity.getxPosition() + (new Random()).nextInt(entity.getxSize());
        item.yPosition = entity.getyPosition() + entity.getySize() + (new Random()).nextInt(30);
        item.addCollisionBox(0, item.xSize, 0, item.ySize);
        item.passable = true;
        item.inInventory = false;
        item.animation = new Animation(x, item, Paths.get("src/main/resources/items/"+item.id+".png"));
        x.getChunkManager().addEntity(item);
    }

    public void drop() {
        inInventory = false;
        xPosition = x.getPlayer().getxPosition() + ((int) x.getPlayer().getxSize()/2);
        yPosition = x.getPlayer().getyPosition() + x.getPlayer().getySize();
        x.getChunkManager().addEntity(this);
    }

    public void collected() { inInventory = true; }

    @Override public void onCollision(Entity entity) { if ( entity instanceof Player ) ((Player) entity).addItem(this); }
    @Override public void render(Renderer renderer) { if ( !inInventory ) renderer.render(animation); }
}
