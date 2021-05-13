package game.entities; 

import game.main.X;
import game.main.render.Animation;
import game.main.render.Renderable;
import java.lang.Math;

/**
 * Entities that are able to move. Movement conversion biases favor higher movement speed to prevent any edge cases where very slow entities stagnate. Movement
 * comprises of setting properties which are used to calculate moves per tick in order to simplify logic and keep movement functionality contained to class.
 */
public abstract class DynamicEntity extends Entity {
    protected double speed; // In pixels per second
    protected double speedModifier;
    protected MovementDirection direction;
    public enum MovementDirection {
        north,
        south,
        east,
        west,
        northeast,
        northwest,
        southeast,
        southwest,
        still
    }

    public void tick(X x) {
        move(); 
    }

    protected void move() {
        double dx = 0;
        double dy = 0;
        
        switch ( direction ) {
            case north:
                dy = -1;
                break;
            case south:
                dy = 1;
                break;
            case east:
                dx = 1;
                break;
            case west:
                dx = -1;
                break;
            case northeast:
                dy = -Math.sqrt(2);
                dx = Math.sqrt(2);
                break;
            case northwest:
                dy = -Math.sqrt(2);
                dx = -Math.sqrt(2);
                break;
            case southeast:
                dy = Math.sqrt(2);
                dx = Math.sqrt(2);
                break;
            case southwest:
                dy = Math.sqrt(2);
                dx = -Math.sqrt(2);
                break;
        }
        
        xPosition += dx*speed*speedModifier;
        yPosition += dy*speed*speedModifier;
    }


    public void setSpeed(int speed) { this.speed = speed; }
    public void setSpeedModifier(double speedModifier) { this.speedModifier = speedModifier; }
    public void setMovementDirection(MovementDirection direction) { this.direction = direction; }
    public MovementDirection getMovementDirection() { return this.direction; }
}
