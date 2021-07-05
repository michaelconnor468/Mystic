package game.entities;

import game.main.X;
import game.main.render.Renderer;
import game.main.render.Animation;
import util.parse.obj.*;

import java.util.HashMap;
import java.nio.file.Paths;

public class StaticEntity extends Entity {
    public StaticEntity() {}
    public StaticEntity(X x, ParserBlock block) {

    }

    public void tick(X x) {}
    public void render(Renderer renderer) { renderer.render(animation); }
    public static StaticEntity load(X x, ParserBlock block) {
        StaticEntity entity = new StaticEntity();
        HashMap<String, ParserObject> props = block.getProperties();
        int type = ((ParserInt) props.get("type")).getNumber();
        ParserBlock template = x.getTemplates("staticEntities").get(type);
        load(x, block, template, entity);
        entity.animation = new Animation(x, entity, Paths.get("src/main/resources/sentity/"+type+".png"));
        return entity;
    }
}
