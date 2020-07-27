package game.main.render;

import game.main.X;

/**
 * Renderable classes can be timed at a different frequency thawn the regular game ticks to improve performance allow implementations of frame rate caps. Renderer
 * object passed to render method as this is a more decoupled approach than having each method interact with a canvas/ other implementation object directly
 */
public interface Renderable {
    public void render(Renderer renderer);
}