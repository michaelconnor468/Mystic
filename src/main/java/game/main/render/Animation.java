package game.main.render;

import game.main.X;
import game.entities.Entity;

public class Animation {
    private Entity entity;
    private int frame;
    private int ticksElapsed;
    private int ticksPerFrame;
    private int totalFrames;

    private Animation() {};
    public Animation(Entity entity) {
        this.entity = entity;
        this.frame = 0;
        this.ticksElapsed = 0;
        this.ticksPerFrame = 1;
    }

    public void animate(Renderer r) {
        // Do stuff
    }

    public void tick(X x) {
        ticksElapsed = ticksElapsed == ticksPerFrame - 1 ? 0 : ticksElapsed + 1;
        if ( ticksElapsed == 0 )
            frame = frame == totalFrames - 1 ? 0 : frame + 1;
    }

    public void setTicksPerFrame(int ticksPerFrame) { this.ticksPerFrame = ticksPerFrame; }
}
