package game.main.render;

import game.main.X;
import game.entities.Entity;
import util.parse.obj.*;

import java.nio.file.Path;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;

public class Animation {
    private int frame;
    private Entity entity;
    private ArrayList<Image> frames;
    private int ticksElapsed;
    private int ticksPerRender;
    private int totalFrames;

    private Animation() {};
    public Animation(X x, Entity entity, Path path) {
        this.entity = entity;
        this.frame = 0;
        this.ticksElapsed = 0;
        this.ticksPerRender = ((ParserInt) x.getMainSettings().getProperties().get("ticksPerRender")).getNumber();
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

    public void tick(X x) {
        ticksElapsed = ticksElapsed == ticksPerRender - 1 ? 0 : ticksElapsed + 1;
        if ( ticksElapsed == 0 )
            frame = frame == totalFrames - 1 ? 0 : frame + 1;
    }

    public void setTicksPerFrame(int ticksPerRender) { this.ticksPerRender = ticksPerRender; }

    public Entity getEntity() { return this.entity; }
    public Image getImage() { return frames.get(frame); }
}
