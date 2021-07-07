package game.player.items;

import util.parse.obj.*;
import game.main.X;
import game.player.Player;
import game.physics.*;
import game.entities.StaticEntity;
import game.entities.Entity;
import game.main.render.Animation;
import game.main.render.Renderer;

import java.util.HashMap;
import java.util.Random;
import java.nio.file.Paths;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Item {
    private X x;
    private String name;
    private int id;
    private boolean inInventory;
    private Item() {}
    
    public Item(X x, int id) {
        this.x = x;
        this.id = id;
        this.name = ((ParserString) x.getTemplates("items").get(id).getProperty("name")).toString();
    }

    public int getId() { return this.id; }
}
