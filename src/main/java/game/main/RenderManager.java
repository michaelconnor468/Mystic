package game.main;

import util.parse.obj.*;
import game.main.render.*;
import game.entities.Player;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;

/**
 * Manages rendering within the game through an object interface Renderer that is passed to objects that must be rendered. Decouples rendering implementations
 * from actual interfaces that objects use. Synchronises the rendering of objects allowing rendering to differ from in game ticks for different refresh rates and performance.
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
        this.framesPerSecond = ((ParserInt) x.getMainSettings().getProperties().get("framesPerSecond")).getNumber();
        this.toRender = new ArrayList<>();
        this.toRenderAbove = new ArrayList<>();
        this.renderTimer = new RenderTimer();
    }

    public void updateGraphicsContext(GraphicsContext gc) { this.renderer = new Renderer(x, gc); }

    public void start() { renderTimer.start(); }
    public void stop() { renderTimer.stop(); }

    public void register(Renderable obj) {
        if ( !toRender.contains(obj) )
            toRender.add(obj);
    }

    public void registerAbovePlayer(Renderable obj) {
        if ( !toRenderAbove.contains(obj) )
            toRenderAbove.add(obj);    
    }

    public void unregister(Renderable obj) {
        if ( toRender.contains(obj) ) 
            toRender.remove(obj);
        else if ( toRenderAbove.contains(obj) )
            toRenderAbove.remove(obj);
    }
    
    private void render() {
        for ( Renderable r : toRender )
            r.render(renderer);
        x.getPlayer().render(renderer);
        for ( Renderable r : toRenderAbove )
            r.render(renderer);
    }

    private class RenderTimer extends AnimationTimer {
        public void handle(long num) { render(); }
    }
}
