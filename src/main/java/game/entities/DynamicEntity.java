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

    protected DynamicEntity() {
        super();
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
                dy = -0.707;
                dx = 0.707;
                break;
            case northwest:
                dy = -0.707;
                dx = -0.707;
                break;
            case southeast:
                dy = 0.707;
                dx = 0.707;
                break;
            case southwest:
                dy = 0.707;
                dx = -0.707;
                break;
        }

        double finalSpeedModifier = speedModifier;
        if ( isSwimming() )
            finalSpeedModifier *= 0.33;

        xPosition += dx*speed*finalSpeedModifier;
        yPosition += dy*speed*finalSpeedModifier;
        if ( x.getChunkManager().isColliding(this) ) {
            xPosition -= dx*speed*finalSpeedModifier;
            yPosition -= dy*speed*finalSpeedModifier;
        }
    }

    public boolean isSwimming() {
        for ( TileEntity entity : x.getChunkManager().getTilesAround(this) )
            if ( !entity.isLiquid() ) return false;
        return true;
    }

    public void setSpeed(int speed) { this.speed = speed; }
    public void setSpeedModifier(double speedModifier) { this.speedModifier = speedModifier; }
    public void setMovementDirection(MovementDirection direction) { this.direction = direction; }
    public MovementDirection getMovementDirection() { return this.direction; }
}
