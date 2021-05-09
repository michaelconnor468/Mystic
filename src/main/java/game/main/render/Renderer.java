package game.main.render;

import game.main.X;
import util.parse.obj.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Provides an interface decoupled from actual rendering logic that can be passed down the chain of objects that need to be rendered to 
 * allow communication with rendering logic.
 */
public class Renderer {
    private X x;
    private GraphicsContext gc; 

    private Renderer() {}
    public Renderer(X x, GraphicsContext gc) {
        this.x = x;
        this.gc = gc;
    }

    public void render(Animation animation) {
        int xCorner = x.getPlayer().getxPosition() + x.getPlayer().getxSize()/2 -  
            (((ParserInt) x.getMainSettings().getProperties().get("resolutionx")).getNumber()/2);
        int yCorner = x.getPlayer().getyPosition() + x.getPlayer().getySize()/2 - 
            (((ParserInt) x.getMainSettings().getProperties().get("resolutiony")).getNumber()/2);
        int xRenderLocation = animation.getEntity().getxPosition() - xCorner; 
        int yRenderLocation = animation.getEntity().getyPosition() - yCorner; 
    }
}
