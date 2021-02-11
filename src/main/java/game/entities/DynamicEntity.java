package game.entities; 

import game.main.X;
import game.main.render.Renderer;
import java.lang.Math;

/**
 * Entities that are able to move. Movement conversion biases favor higher movement speed to prevent any edge cases where very slow entities stagnate. Movement
 * comprises of setting properties which are used to calculate moves per tick in order to simplify logic and keep movement functionality contained to class.
 */
public class DynamicEntity extends Entity {
    private int speed; // In pixels per tick
    private double speedModifier;
    private double movementAngle;

    public void tick(X x) {
        move();
    }

    public void render(Renderer renderer) {

    }

    private void move() {
        xPosition = xPosition + (int) Math.ceil(speed*speedModifier*Math.cos(movementAngle));
        yPosition = yPosition + (int) Math.ceil(speed*speedModifier*Math.sin(movementAngle));
    }

    public void setSpeed(int speed) { this.speed = speed; }
    public void setSpeedModifier(double speedModifier) { this.speedModifier = speedModifier; }
    public void setMovementAngle(double movementAngle) { this.movementAngle = movementAngle; }
}
