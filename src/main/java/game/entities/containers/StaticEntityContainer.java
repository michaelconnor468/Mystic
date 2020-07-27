package game.entities.containers;

import java.util.ArrayList;
import game.main.X;
import game.entities.StaticEntity;
import game.main.render.Renderer;

public class StaticEntityContainer extends EntityContainer<StaticEntity> {
    public void addEntity( StaticEntity entity ) {
        //TODO
    }

    public void removeEntity( StaticEntity entity ) {
        //TODO
    }

    public void tick(X x) {
        //TODO
    }

    public void render(Renderer renderer) {
        // TODO
    }

    public ArrayList<StaticEntity> getAllEntities() {
        ArrayList<StaticEntity> returnList = new ArrayList<>();
        //TODO
        return returnList;
    }

    public ArrayList<StaticEntity> getEntitiesWithinRange(double minX, double maxX, double minY, double maxY) {
        ArrayList<StaticEntity> returnList = new ArrayList<>();
        //TODO
        return returnList;
    }
}