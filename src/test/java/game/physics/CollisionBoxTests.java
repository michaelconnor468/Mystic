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
            public void onCollision(Collidable entity) { CollisionBoxTests.this.collided1 = true;  }
            public void addCollisionBox(Point min, Point max) { boxes.add(new CollisionBox(this, min, max, false)); }
            public Collection<CollisionBox> getCollisionBoxes() { return boxes; }
        };
        entity2 = new Collidable() {
            private ArrayList<CollisionBox> boxes = new ArrayList<>();;
            public Point getPosition() { return new Point(150, 150); }
            public Point getSize() { return new Point(100, 100); }
            public void onCollision(Collidable entity) { CollisionBoxTests.this.collided2 = true;  }
            public void addCollisionBox(Point min, Point max) { boxes.add(new CollisionBox(this, min, max, false)); }
            public Collection<CollisionBox> getCollisionBoxes() { return boxes; }
        };
        collided1 = false;
        collided2 = false;
    }

    @Test public void initializationTest() {
        String errormsg = "Failed to correctly initialize Collision Box";
        assertNotNull(new CollisionBox(entity1, new Point(0, 0), new Point(100, 100), false), errormsg);
        assertNotNull(new CollisionBox(entity1, new Point(-100, -100), new Point(100, 100), false), errormsg);
        assertNotNull(new CollisionBox(entity1, new Point(-100, -100), new Point(-10, -10), false), errormsg);
    }

    @Test public void doesNotCollideTest() {
        String errormsg = "Collision detected in collidable entities that were not meant to collide";

        entity1.addCollisionBox(new CollisionBox(entity1, new Point(0, 0), new Point(100, 100), true));
        entity1.addCollisionBox(new CollisionBox(entity1, new Point(20, 20), new Point(100, 100), true));
        entity2.addCollisionBox(new CollisionBox(entity1, new Point(0, 0), new Point(100, 100), true));
        entity2.addCollisionBox(new CollisionBox(entity1, new Point(-30, -30), new Point(40, 40), false));

        assertFalse(entity1.testCollision(entity2), errormsg);
        assertFalse(entity2.testCollision(entity1), errormsg);
    }

    @Test public void collisionTest() {
        String errormsg = "Entities with overlapping CollisionBoxes are not colliding";

        entity1.addCollisionBox(new CollisionBox(entity1, new Point(0, 0), new Point(100, 100), true));
        entity1.addCollisionBox(new CollisionBox(entity1, new Point(-10, -10), new Point(100, 100), true));

        assertTrue(entity1.testCollision(entity2), errormsg);
        assertTrue(entity2.testCollision(entity1), errormsg);
        
        errormsg = "Collision event not called within one of the collidable entities taking place in a collision";
        assertTrue(collided1, errormsg);
        assertTrue(collided2, errormsg);
    }

    @Test public void passableCollisionEventTest() {
        entity1.addCollisionBox(new CollisionBox(entity1, new Point(0, 0), new Point(100, 100), false));
        entity1.addCollisionBox(new CollisionBox(entity1, new Point(-10, -10), new Point(100, 100), false));

        entity1.testCollision(entity2);
        entity2.testCollision(entity1);

        String errormsg = "Collision event not called within one of the collidable entities taking place in a collision";
        assertTrue(collided1, errormsg);
        assertTrue(collided2, errormsg);
    }
}
