package game.entities; 

import game.main.X;
import game.main.render.Animation;
import game.main.render.Renderable;
import game.entities.buffs.Buff;
import util.parse.obj.*;

import java.lang.Math;
import java.nio.file.Paths;

/**
 * Entities that are able to move. Movement conversion biases favor higher movement speed to prevent any edge cases where very slow entities stagnate. Movement
 * comprises of setting properties which are used to calculate moves per tick in order to simplify logic and keep movement functionality contained to class.
 */
public abstract class DynamicEntity extends Entity {
    protected Animation walkNorthAnimation;
    protected Animation walkNorthWestAnimation;
    protected Animation walkNorthEastAnimation;
    protected Animation walkSouthAnimation;
    protected Animation walkSouthWestAnimation;
    protected Animation walkSouthEastAnimation;
    protected Animation walkWestAnimation;
    protected Animation walkEastAnimation;

    protected double directionAngle;
    protected double speed; 
    protected double stamina;
    protected double maxStamina;
    protected MovementDirection direction;
    protected boolean stationary;
    public enum MovementDirection {
        north,
        south,
        east,
        west,
        northeast,
        northwest,
        southeast,
        southwest
    }

    protected DynamicEntity() {}
    public DynamicEntity(X x, ParserBlock block, ParserBlock template) {
        super(x, block, template);
        this.stamina = ((ParserInt) loadProperty(block, template, "stamina")).getNumber();
        this.maxStamina = ((ParserInt) loadProperty(block, template, "maxStamina")).getNumber();
        this.direction = MovementDirection.west;
        this.stationary = true;
        this.speed = ((ParserInt) loadProperty(block, template, "speed")).getNumber();
        this.walkNorthAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_north.png"));
        this.walkNorthWestAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_northwest.png"));
        this.walkNorthEastAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_northeast.png"));
        this.walkSouthAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_south.png"));
        this.walkSouthWestAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_southwest.png"));
        this.walkSouthEastAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_southeast.png"));
        this.walkWestAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_west.png"));
        this.walkEastAnimation = 
            new Animation(x, this, Paths.get("src/main/resources/dentity/" + type + "/walk_east.png"));
        this.animation = this.walkSouthEastAnimation;
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

        if ( stationary ) {
            dx = 0;
            dy = 0;
        }

        if ( isSwimming() ) addBuff(Buff.load(x, this, "swimming"));
        
        double finalSpeedModifier = 1;
        for ( Buff buff : buffs ) finalSpeedModifier *= buff.getSpeedModifier(); 

        if ( dx == 0 && dy == 0 ) return;

        position.setLocation(position.getX()+dx*speed*finalSpeedModifier, position.getY()+dy*speed*finalSpeedModifier);
        if ( x.getChunkManager().testCollision(this) ) position.setLocation(position.getX()-dx*speed*finalSpeedModifier, 
            position.getY()-dy*speed*finalSpeedModifier);
    }

    public boolean isSwimming() {
        for ( TileEntity entity : x.getChunkManager().getTilesAround(this) ) {
            if ( !entity.isLiquid() ) return false;
        }
        return true;
    }

    public void run() {
        if ( stamina > 1 )
            addBuff(Buff.load(x, this, "running"));
    }

    public void setSpeed(int speed) { this.speed = speed; }
    public void setMovementDirection(MovementDirection direction) { this.direction = direction; }
    public void setDirectionAngle(double angle) {
        this.directionAngle = angle;
        if ( angle >= 340 || angle < 20 ) setMovementDirection(MovementDirection.north);
        if ( angle >= 20 && angle < 70 ) setMovementDirection(MovementDirection.northeast);
        if ( angle >= 290 && angle < 340 ) setMovementDirection(MovementDirection.northwest);
        if ( angle >= 70 && angle < 110 ) setMovementDirection(MovementDirection.east);
        if ( angle >= 250 && angle < 290 ) setMovementDirection(MovementDirection.west);
        if ( angle >= 110 && angle < 160 ) setMovementDirection(MovementDirection.southeast);
        if ( angle >= 190 && angle < 240 ) setMovementDirection(MovementDirection.southwest);
        if ( angle >= 160 && angle < 190 ) setMovementDirection(MovementDirection.south);
    }
    public double getDirectionAngle() { return this.directionAngle; }
    public double getStamina() { return this.stamina; }
    public double getMaxStamina() { return this.maxStamina; }
    public MovementDirection getMovementDirection() { return this.direction; }
    public boolean isStationary() { return stationary; }
    public void toggleStationary() { stationary = !stationary; }
    public void setStationary(boolean stationary) { this.stationary = stationary; }

    public ParserBlock save(ParserBlock block) {
        super.save(block);
        block.addProperty(new ParserProperty("stamina", new ParserInt((int) this.stamina)));
        block.addProperty(new ParserProperty("maxStamina", new ParserInt((int) this.maxStamina)));
        block.addProperty(new ParserProperty("speed", new ParserInt((int) this.speed)));
        return block;
    }
}
