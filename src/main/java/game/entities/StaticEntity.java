package game.entities;

import game.main.X;
import game.main.render.Renderer;
import util.parse.obj.*;

public class StaticEntity extends Entity {
    public void tick(X x) {}
    public void render(Renderer renderer) {}
    public static StaticEntity load(X x, ParserBlock block) {
        StaticEntity entity = new StaticEntity();
        load(x, block, entity);
        return entity;
    }
}
