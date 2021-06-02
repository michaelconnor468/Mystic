package game.main;

import util.parse.obj.*;
import game.main.render.*;
import game.player.Player;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;

/**
 * Manages timing of rendering and creation of renderer for use in dependancy injection for entities to register
 * animations to render. Allows entities with the renderer interfact to be called to provide an animation at
 * render time in order to decouple timing from render logici and provide an interface to easily start and stop
 * rendering based on game state changes.
 */
public class RenderManager {
    private X x;
    private int framesPerSecond;
    private RenderTimer renderTimer;
    private ArrayList<Renderable> toRender, toRenderAbove;
    private Renderer renderer;
    private Player player;

    private RenderManager() {}
    public RenderManager(X x) {
        this.x = x;
        this.framesPerSecond = ((ParserInt) x.getMainSettings().get("framesPerSecond")).getNumber();
        this.toRender = new ArrayList<>();
        this.toRenderAbove = new ArrayList<>();
        this.renderTimer = new RenderTimer();
    }

    /**
     * Allows any arbitrary GUI element to render the game by simply registering it's graphics context with the class
     * in order to provide flexibility to implement the gui and draw anywhere with ease.
     */
    public void updateGraphicsContext(GraphicsContext gc) { this.renderer = new Renderer(x, gc); }

    public void start() { renderTimer.start(); }
    public void stop() { renderTimer.stop(); }

    public void register(Renderable obj) {
        if ( !toRender.contains(obj) )
            toRender.add(obj);
    }
    
    public void unregister(Renderable obj) {
        if ( toRender.contains(obj) ) 
            toRender.remove(obj);
        else if ( toRenderAbove.contains(obj) )
            toRenderAbove.remove(obj);
    }
    
    /**
     * Ensures all entities register first before drawing within any given render cycle in order for the renderer to
     * properly order them for rendering so that correct depth is acheived.
     */
    private void render() {
        for ( Renderable r : toRender )
            r.render(renderer);
        x.getPlayer().render(renderer);
        renderer.draw();
    }

    private class RenderTimer extends AnimationTimer {
        public void handle(long num) { render(); }
    }
}
