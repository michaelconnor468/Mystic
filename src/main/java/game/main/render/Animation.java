package game.main.render;

import game.main.X;
import game.entities.Entity;
import game.main.TickObserver;
import util.parse.obj.*;

import java.nio.file.Path;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;

/**
 * Used for image storing and processing into a successive stream of images that form an onscreen animation. Is not made
 * to handle any logic pertaining to rendering but only to store and cycle through images. The default image when not
 * animation is set to the first animation frame for consistancy accross iterations.
 */
public class Animation implements TickObserver {
    private int frame;
    private Entity entity;
    private ArrayList<Image> frames;
    private boolean still;
    private int ticksElapsed;
    private int framesPerSecond, ticksPerSecond;
    private int totalFrames;

    private Animation() {};

    /**
     * Forms an animation out of the successive image slices along the y-axis of one large input image. Suxh an image
     * height and width should be consistant with the size of its corresponding entity. However the width can be any
     * multiple of it so as to not hardcode the length of the animation but leave it to be configured by the width
     * of the animation image.
     */
    public Animation(X x, Entity entity, Path path) {
        this.entity = entity;
        this.still = false;
        this.frame = 0;
        this.ticksElapsed = 0;
        this.ticksPerSecond = ((ParserInt) x.getMainSettings().getProperties().get("ticksPerSecond")).getNumber();
        this.framesPerSecond = 60; 
        this.frames = new ArrayList<>();
        try {
            Image loadedImage = new Image(path.toUri().toURL().toString());
            int imgWidth = (int) loadedImage.getWidth();
            int imgHeight = (int) loadedImage.getHeight();
            assert imgHeight == entity.getySize() : "image height incompatible with entity height";
            assert imgWidth % entity.getxSize() == 0 : "image width not divisible by entity width";
            PixelReader reader = loadedImage.getPixelReader();
            for ( int i = 0; i < imgWidth/entity.getxSize(); i++ ) 
                frames.add(new WritableImage(reader, i*imgWidth, 0, imgWidth, imgHeight));
        } catch ( Exception e ) { e.printStackTrace(new java.io.PrintStream(System.err)); System.exit(0); }
    }

    /**
     * Cycles through the frames of an image to animate. This is tied to the standard tick timing and configured
     * by a setting parameter so as to not introduce additional timers for the sole purpose of cycling through
     * animation frames.
     */
    public void tick(X x) {
        ticksElapsed = ticksElapsed/ticksPerSecond >= 1/framesPerSecond ? 0 : ticksElapsed + 1;
        if ( ticksElapsed == 0 )
            frame = frame >= totalFrames - 1 ? 0 : frame + 1;
    }

    public Entity getEntity() { return this.entity; }
    public Image getImage() { 
        if ( still )
            return frames.get(0);
        return frames.get(frame); 
    }
    public void setStill(boolean still) {
        ticksElapsed = 0;
        this.still = still;
    }
}
