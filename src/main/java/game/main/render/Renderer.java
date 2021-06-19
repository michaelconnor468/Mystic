package game.main.render;

import game.main.X;
import game.physics.Positionable;
import game.entities.TileEntity;
import game.entities.DynamicEntity;
import util.parse.obj.*;

import static java.util.Comparator.comparing;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Buffers entities for rendering and then renders them in the order of their bottom y coordinates to create a layered
 * depth effect. Decouples all the logic from the entities themselves which need only register an animation for drawing.
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
        this.resolutionx = ((ParserInt) x.getMainSettings().get("resolutionx")).getNumber(); 
        this.resolutiony = ((ParserInt) x.getMainSettings().get("resolutiony")).getNumber(); 
        this.tileEntities = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public void render(Animation animation) {
        if ( animation.getEntity() instanceof TileEntity )
            tileEntities.add(animation);
        else
            entities.add(animation);
    }

    /**
     * Clears render queues and draws out entities. Made to call as a separate function to ensure entity queues
     * are fully populated before each render with the desired entities.
     */
    public void draw() {
        for ( Animation a : tileEntities )
            drawAnimation(a);
        tileEntities = new ArrayList<>();
        entities.sort((a1, a2) -> { 
            return (int) ((a1.getEntity().getPosition().getY() + a1.getEntity().getSize().getY()) 
                - (a2.getEntity().getPosition().getY() + a2.getEntity().getSize().getY())); 
        });
        for ( Animation a : entities ) 
            drawAnimation(a);
        entities = new ArrayList<>();
    }

    /**
     * Performs calculations to only render what is visible based off of the location of the player in order to ensure
     * centering of the camera on them at all times and that off-screen entities do not cause issues during rendering.
     */
    private void drawAnimation(Animation animation) {
        Positionable e = animation.getEntity();
        int xCorner = (int) (x.getPlayer().getPosition().getX() + x.getPlayer().getSize().getX()/2 - (resolutionx/2));
        int xRenderLocation = (int) (e.getPosition().getX() - xCorner); 
        if ( xRenderLocation > resolutionx || xRenderLocation + (int) e.getSize().getX() < 0 )
            return;
        int yCorner = (int) (x.getPlayer().getPosition().getY() + x.getPlayer().getSize().getY()/2 - (resolutiony/2));
        int yRenderLocation = (int) e.getPosition().getY() - yCorner; 
        if ( yRenderLocation > resolutiony || yRenderLocation + (int) e.getSize().getY() < 0 )
            return;
        Image image = animation.getImage();
        if ( e instanceof DynamicEntity && ((DynamicEntity) e).isSwimming() ) 
            image = new WritableImage(image.getPixelReader(), 0, 0, 
                (int) e.getSize().getX(), (int) (e.getSize().getY()*0.28));
        gc.drawImage(image, xRenderLocation, yRenderLocation);
    }
}
