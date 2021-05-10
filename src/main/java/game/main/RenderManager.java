package game.main;

import util.parse.obj.*;
import game.main.render.*;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Manages rendering within the game through an object interface Renderer that is passed to objects that must be rendered. Decouples rendering implementations
 * from actual interfaces that objects use. Synchronises the rendering of objects allowing rendering to differ from in game ticks for different refresh rates and performance.
 */
public class RenderManager implements TickObserver {
    private X x;
    private int ticksPerRender, ticksElapsed;
    private ArrayList<Renderable> toRender;
    private Renderer renderer;

    private RenderManager() {}
    public RenderManager(X x) {
        this.x = x;
        this.ticksPerRender = ((ParserInt) x.getMainSettings().getProperties().get("ticksPerRender")).getNumber();
        this.ticksElapsed = 0;
        this.toRender = new ArrayList<>();
        x.getTimingManager().register(this);
    }

    public void updateGraphicsContext(GraphicsContext gc) { this.renderer = new Renderer(x, gc); }

    public void tick(X x) {
        if ( ticksPerRender == ticksElapsed++ )
            render();
    }

    public void register(Renderable obj) {
        if ( !toRender.contains(obj) )
            toRender.add(obj);
    }

    public void unregister(Renderable obj) {
        toRender.remove(obj);
    }

    private void render() {
        for ( Renderable r : toRender )
            r.render(renderer);
    }
}
