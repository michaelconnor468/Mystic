package game.main.render;

import game.main.X;

/**
 * Interface used to identify and manage entities that can be rendered. Interface called when it is time to render and
 * provides the entity with a renderer which it can register an animation to render at the time or not. Decouples
 * actual render logic from entities due to considerations such as render order and position on the screen relative
 * to the player that actual rendering is based upon.
 */
public interface Renderable {
    public void render(Renderer r); 
}
