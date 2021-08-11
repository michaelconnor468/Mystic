package game.main;

import game.entities.Entity;
import game.player.Player;
import util.parse.FileParser;
import util.parse.obj.ParserBlock;
import util.parse.obj.ParserObject;

import java.util.HashMap;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.stage.Stage;
import javafx.application.Application;

/**
 * Class represents the context of a game which holds global variables and services. Since the same one will be passed 
 * through the tree of all classes, care should be taken whenever modifiying any variables and many should 
 * remain read-only.
 */
public class X {
    private Game game;
    private WindowManager windowManager;
    private GameStateManager gameStateManager;
    private ChunkManager chunkManager;
    private Application application;
    private HashMap<String, ParserObject> mainSettings;
    private RenderManager renderManager;
    private TimingManager timingManager;
    private HashMap<String, HashMap<Integer, ParserBlock>> templates;
    private HashMap<String, ParserObject> buffs;
    private Player player;

    public X() {}

    public Game getGame() { return game; }
    public void createGameSingleton(double ticksPerSecond) {
        if ( game == null )
            game = new Game(this, ticksPerSecond);
    }

    public WindowManager getWindowManager() { return windowManager; }
    public void createWindowManagerSingleton(Stage stage) { 
        if ( windowManager == null ) 
            windowManager = new WindowManager(this, stage);
    }

    public GameStateManager getGameStateManager() { return gameStateManager; }
    public void createGameStateManagerSingleton() {
        if ( gameStateManager == null )
            gameStateManager = new GameStateManager(this);
    }

    public Application getApplication() { return application; }
    public void createApplicationSingleton(Application application) {
        if ( this.application == null )
            this.application = application;
    }

    public ChunkManager getChunkManager() { return chunkManager; }
    public void createChunkManager() { 
        chunkManager = new ChunkManager(this); 
    }

    public void createSettingsSingletons(Path path) {
        if ( mainSettings == null ) 
            mainSettings = FileParser.parse(path.resolve(Paths.get("main.mcfg"))).getProperties();
        if ( buffs == null ) buffs = FileParser.parse(path.resolve(Paths.get("buffs.mcfg"))).getProperties();
    }
    public void populateTemplates(Path path) {
        if ( templates == null ) templates  = new HashMap<>();
        try { 
            templates.put("staticEntities", loadTemplates( Files.walk(path.resolve(Paths.get("sentity")))));
            templates.put("dynamicEntities", loadTemplates( Files.walk(path.resolve(Paths.get("dentity")))));
            templates.put("melee", loadTemplates( Files.walk(path.resolve(Paths.get("weapons/melee")))));
            templates.put("ranged", loadTemplates( Files.walk(path.resolve(Paths.get("weapons/ranged")))));
            templates.put("items", loadTemplates( Files.walk(path.resolve(Paths.get("items")))));
        } catch ( Exception e ) {
            System.err.println("Failed to read template files\n" + e);
            System.exit(1);
        }
    }
    private HashMap<Integer, ParserBlock> loadTemplates(Stream<Path> paths) {
        HashMap<Integer, ParserBlock> retmap = new HashMap<>();
        Pattern pattern = Pattern.compile("[0-9]+\\.mcfg");
        paths.forEach(f -> {
            Matcher matcher = pattern.matcher(f.toString());
            if ( matcher.find() ) {
                String name = matcher.group();
                retmap.put(Integer.parseInt(name.substring(0, name.length() - 5)), FileParser.parse(f)); 
            }
        });
        return retmap;
    }
    public HashMap<Integer, ParserBlock> getTemplates(String str) {
        return templates.containsKey(str) ? templates.get(str) : null;
    }
    public HashMap<String, ParserObject> getMainSettings() { return mainSettings; }

    public void createPlayer(Path path) {
        if ( player != null ) player.destroy();
        player = new Player(this, FileParser.parse(path.resolve("player/player.msv")), 
            FileParser.parse(Paths.get("src/main/config/templates/player/player.mcfg"))); 
    }

    public HashMap<String, ParserObject> getBuffs() { return buffs; }
    public Player getPlayer() { return player; }
    public void createRenderManager() { renderManager = new RenderManager(this); }
    public RenderManager getRenderManager() { return renderManager; };
    public void createTimingManager() { timingManager = new TimingManager(this); }
    public TimingManager getTimingManager() { return timingManager; }
}
