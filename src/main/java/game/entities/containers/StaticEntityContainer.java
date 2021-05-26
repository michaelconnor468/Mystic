package game.entities.containers;

import java.util.ArrayList;
import game.main.X;
import game.entities.StaticEntity;
import game.main.render.Renderer;

/**
 * Currently a type wrapper and no new functionality is added.
 */
public class StaticEntityContainer extends EntityContainer<StaticEntity> {
    private StaticEntityContainer() {}
    public StaticEntityContainer(X x) { super(x); }
}
