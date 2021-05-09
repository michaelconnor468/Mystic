package game.main.render;

import game.main.X;

import javafx.scene.canvas.GraphicsContext;

/**
 * Provides an interface decoupled from actual rendering logic that can be passed down the chain of objects that need to be rendered to 
 * allow communication with rendering logic.
 */
public class Renderer {
    X x;
    GraphicsContext gc;

    private Renderer() {}
    public Renderer(X x, GraphicsContext gc) {
        this.x = x;
        this.gc = gc;
    }

    public void render(Animation animation) {

    }
}
