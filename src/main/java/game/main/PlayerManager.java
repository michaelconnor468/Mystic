package game.main;

import game.entities.Chunk;
import game.entities.Entity;
import game.entities.TileEntity;
import game.main.render.Renderable;
import game.main.render.Renderer;
import util.parse.BlockParser;
import util.parse.obj.ParserBlock;
import util.parse.obj.ParserInt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.concurrent.Task;
import java.awt.Point;

/**
 * Manages remote players and handles network communication for peer-to-peer interactions.
 */
public class PlayerManager implements TickObserver, Renderable {
    private X x;
    private List<Player> foreignPlayers;

    private ChunkManager() {}

    public ChunkManager(X x) {
        this.x = x;
        x.getTimingManager().register(this);
        x.getRenderManager().register(this);
        this.listenForConnections();
    }

    public void tick(X x) {
        for ( Player player : foreignPlayers ) player.tick(x);
    }

    public void render(Renderer r) {
        for ( Player player : foreignPlayers ) player.render(r);
    }
}
