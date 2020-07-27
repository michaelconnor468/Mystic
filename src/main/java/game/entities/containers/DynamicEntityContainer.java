package game.entities.containers;

import game.entities.DynamicEntity;
import game.main.X;
import game.main.render.Renderer;

import java.util.ArrayList;

public class DynamicEntityContainer extends EntityContainer<DynamicEntity> {

    public void addEntity( DynamicEntity entity ) {
        //TODO
    }

    public void removeEntity( DynamicEntity entity ) {
        //TODO
    }

    public ArrayList<DynamicEntity> getAllEntities() {
        //TODO
        ArrayList<DynamicEntity> returnList = new ArrayList<>();
        return returnList;
    }

    public ArrayList<DynamicEntity> getEntitiesWithinRange(double minX, double maxX, double minY, double maxY) {
        //TODO
        ArrayList<DynamicEntity> returnList = new ArrayList<>();
        return returnList;
    }

    public void tick( X x ) {
        //TODO
    }

    public void render( Renderer renderer ) {
        //TODO
    }
}