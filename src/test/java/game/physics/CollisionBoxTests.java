package game.physics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.Collection;
import java.util.ArrayList;

public class CollisionBoxTests {
    private Collidable entity1, entity2;
    private boolean collided1, collided2;

    @BeforeEach public void setup() {
        entity1 = new Collidable() {
            private ArrayList<CollisionBox> boxes = new ArrayList<>();;
            public Point getPosition() { return new Point(100, 100); } 
            public Point getSize() { return new Point(100, 100); }
            public void onCollision(Collidable entity) { CollisionBoxTests.this.collided1 = true; }
            public void addCollisionBox(Point min, Point max) { boxes.add(new CollisionBox(this, min, max, false)); }
            public Collection<CollisionBox> getCollisionBoxes() { return boxes; }
        };
        entity2 = new Collidable() {
            private ArrayList<CollisionBox> boxes = new ArrayList<>();;
            public Point getPosition() { return new Point(150, 150); }
            public Point getSize() { return new Point(100, 100); }
            public void onCollision(Collidable entity) { CollisionBoxTests.this.collided2 = true; }
            public void addCollisionBox(Point min, Point max) { boxes.add(new CollisionBox(this, min, max, false)); }
            public Collection<CollisionBox> getCollisionBoxes() { return boxes; }
        };
        collided1 = false;
        collided2 = false;
    }

    @Test public void initializationTest() {
        String errormsg = "Failed to correctly initialize Collision Box";
    }
}
