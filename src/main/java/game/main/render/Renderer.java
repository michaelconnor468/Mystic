package game.main.render;

import game.main.X;
import game.entities.TileEntity;
import util.parse.obj.*;

import static java.util.Comparator.comparing;
import java.util.ArrayList;
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
    private ArrayList<Animation> tileEntities;
    private ArrayList<Animation> entities;

    private Renderer() {}
    public Renderer(X x, GraphicsContext gc) {
        this.x = x;
        this.gc = gc;
        this.resolutionx = ((ParserInt) x.getMainSettings().getProperties().get("resolutionx")).getNumber(); 
        this.resolutiony = ((ParserInt) x.getMainSettings().getProperties().get("resolutiony")).getNumber(); 
        this.tileEntities = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public void render(Animation animation) {
        if ( animation.getEntity() instanceof TileEntity )
            tileEntities.add(animation);
        else
            entities.add(animation);
    }

    public void draw() {
        for ( Animation a : tileEntities )
            drawAnimation(a);
        tileEntities = new ArrayList<>();
        entities.sort((a1, a2) -> { 
            return (a1.getEntity().getyPosition() + a1.getEntity().getySize()) 
                - (a2.getEntity().getyPosition() + a2.getEntity().getySize()); 
        });
        for ( Animation a : entities ) 
            drawAnimation(a);
        entities = new ArrayList<>();
    }

    private void drawAnimation(Animation animation) {
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
