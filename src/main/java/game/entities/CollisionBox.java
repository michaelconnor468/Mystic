package game.entities;

import util.parse.obj.*;

import java.util.Map;
import java.awt.Point;

public class CollisionBox {
    private Point min, max;
    private Entity entity;

    private CollisionBox() {}

    public CollisionBox(Entity entity, ParserBlock block) {
        Map<String, ParserObject> props = block.getProperties();
        this.min = new Point(((ParserInt) props.get("xMin")).getNumber(), ((ParserInt) props.get("xMax")).getNumber());
        this.max = new Point(((ParserInt) props.get("yMin")).getNumber(), ((ParserInt) props.get("yMax")).getNumber());
        this.entity = entity;
    }

    public CollisionBox(Entity entity, Point min, Point max) {
        this.min = new Point(min);
        this.max = new Point(max);
        this.entity = entity;
    }

    public boolean collidesWith(CollisionBox collisionBox) {
        return !(
            this.getRealMax().getX() < collisionBox.getRealMin().getX() ||
            this.getRealMin().getX() > collisionBox.getRealMax().getX() ||
            this.getRealMax().getY() < collisionBox.getRealMin().getY() ||
            this.getRealMin().getY() > collisionBox.getRealMax().getY() 
        );
    }

    public Point getRealMin() { 
        Point ret = new Point(entity.getPosition()); 
        ret.translate((int) min.getX(), (int) min.getY()); 
        return ret;
    }

    public Point getRealMax() { 
        Point ret = new Point(entity.getPosition()); 
        ret.translate((int) max.getX(), (int) max.getY()); 
        return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("xMin: " + min.getX());
        sb.append(",xMax: " + max.getX());
        sb.append(",yMin: " + min.getY());
        sb.append(",yMax: " + max.getY());
        sb.append("}");
        return sb.toString();
    };
}
