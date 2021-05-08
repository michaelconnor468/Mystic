package game.main.render;

import java.nio.file.Path;

import game.main.X;
import game.entities.Entity;
import util.parse.obj.*;

public class Animation {
    private int frame;
    private Entity entity;
    private int ticksElapsed;
    private int ticksPerFrame;
    private int totalFrames;

    private Animation() {};
    public Animation(X x, Entity entity, Path path) {
        this.entity = entity;
        this.frame = 0;
        this.ticksElapsed = 0;
        this.ticksPerFrame = ((ParserInt) x.getMainSettings().getProperties().get("ticksPerFrame")).getNumber();
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
