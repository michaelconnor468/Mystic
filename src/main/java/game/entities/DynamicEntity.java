package game.entities; 

import game.main.X;
import game.main.render.Animation;
import game.main.render.Renderable;
import game.entities.buffs.Buff;
import java.lang.Math;

/**
 * Entities that are able to move. Movement conversion biases favor higher movement speed to prevent any edge cases where very slow entities stagnate. Movement
 * comprises of setting properties which are used to calculate moves per tick in order to simplify logic and keep movement functionality contained to class.
 */
public abstract class DynamicEntity extends Entity {
    protected double speed; 
    protected double stamina;
    protected double maxStamina;
    protected double health;
    protected double maxHealth;
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
        super.tick(x);
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

        if ( isSwimming() )
            addBuff(Buff.load(x, this, "swimming"));
        
        double finalSpeedModifier = 1;
        for ( Buff buff : buffs )
            finalSpeedModifier *= buff.getSpeedModifier(); 

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

    public void run() {
        if ( stamina > 1 )
            addBuff(Buff.load(x, this, "running"));
    }

    public void setSpeed(int speed) { this.speed = speed; }
    public void setMovementDirection(MovementDirection direction) { this.direction = direction; }
    public double getHealth() { return this.health; }
    public double getMaxHealth() { return this.maxHealth; }
    public double getStamina() { return this.stamina; }
    public double getMaxStamina() { return this.maxStamina; }
    public MovementDirection getMovementDirection() { return this.direction; }
}
