package game.entities.containers;

import java.util.ArrayList;
import game.main.X;
import game.entities.StaticEntity;
import game.main.render.Renderer;
import util.parse.obj.*;

/**
 * Currently a type wrapper and no new functionality is added.
 */
public class StaticEntityContainer extends EntityContainer<StaticEntity> {
    private StaticEntityContainer() {}
    public StaticEntityContainer(X x) { super(x); }

    public ParserBlock save(ParserBlock block) {
        ParserArray array = new ParserArray(ParserObject.ObjectType.BLOCK);
        for ( StaticEntity e : entities ) if ( e.isSaveable() ) array.add(e.save(new ParserBlock()));
        block.addProperty(new ParserProperty("staticEntities", array));
        return block;
    }
}
