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
        super(x, block, 
            x.getTemplates("staticEntities").get(((ParserInt) block.getProperties().get("type")).getNumber()));
        this.type = ((ParserInt) block.getProperty("type")).getNumber();
        animation = new Animation(x, this, Paths.get("src/main/resources/sentity/"+type+".png"));
    }
    public StaticEntity(X x, ParserBlock block, ParserBlock template) {
        super(x, block, template);
        this.type = ((ParserInt) block.getProperties().get("type")).getNumber();
        animation = new Animation(x, this, Paths.get("src/main/resources/sentity/"+type+".png"));
    }

    public void tick(X x) {}
    public void render(Renderer renderer) { renderer.render(animation); }

    public ParserBlock save(ParserBlock block) {
        block.getProperties().put("type", new ParserInt(type));
        return super.save(block);
    }
}
