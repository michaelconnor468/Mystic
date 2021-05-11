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
    private int resolutionx, resolutiony;

    private Renderer() {}
    public Renderer(X x, GraphicsContext gc) {
        this.x = x;
        this.gc = gc;
        this.resolutionx = ((ParserInt) x.getMainSettings().getProperties().get("resolutionx")).getNumber(); 
        this.resolutiony = ((ParserInt) x.getMainSettings().getProperties().get("resolutiony")).getNumber(); 
    }

    public void render(Animation animation) {
        int xCorner = x.getPlayer().getxPosition() + x.getPlayer().getxSize()/2 - (resolutionx/2);
        int xRenderLocation = animation.getEntity().getxPosition() - xCorner; 
        if ( xRenderLocation > resolutionx || xRenderLocation + animation.getEntity().getxSize() < 0 )
            return;
        int yCorner = x.getPlayer().getyPosition() + x.getPlayer().getySize()/2 - (resolutiony/2);
        int yRenderLocation = animation.getEntity().getyPosition() - yCorner; 
        if ( yRenderLocation > resolutiony || yRenderLocation + animation.getEntity().getySize() < 0 )
            return;
        gc.drawImage(animation.getImage(), xRenderLocation, yRenderLocation);
    }
}
