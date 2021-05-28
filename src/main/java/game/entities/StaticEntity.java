package game.entities;

import game.main.X;
import game.main.render.Renderer;
import util.parse.obj.*;

import java.util.HashMap;

public class StaticEntity extends Entity {
    public void tick(X x) {}
    public void render(Renderer renderer) {}
    public static StaticEntity load(X x, ParserBlock block) {
        StaticEntity entity = new StaticEntity();
        HashMap<String, ParserObject> props = block.getProperties();
        ParserBlock template = x.getTemplate("staticEntities").get(((ParserInt) props.get("type")).getNumber());
        load(x, block, template, entity);
        return entity;
    }
}
