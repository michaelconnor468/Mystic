package game.main;

import util.parse.FileParser;
import util.parse.obj.*;

import java.nio.file.Path; 
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChunkManagerTests {
    static X x;

    @BeforeAll
    public static void initialize() {
        x = new X();
        x.createSettingsSingletons(Paths.get("src/test/config/settings"));
        x.createTimingManager();
        x.createRenderManager();
        x.createChunkManager();
        x.createPlayer(Paths.get("src/test/config/worlds/default"));
        x.getChunkManager().loadChunks(Paths.get("src/test/config/worlds/default"));
    }

    @Test
    public void chunksLoadedTest() {
        assertEquals(64, x.getChunkManager().getChunkSize(), "Incorrect chunk size setting loaded");
        assertEquals(64, x.getChunkManager().getTileSize(), "Incorrect tile size setting loaded");
        assertEquals(1, x.getChunkManager().getChunkLoadDiameter(), "Incorrect chunk load diameter setting loaded");
        
        assertEquals(9, x.getChunkManager().getChunks().size(), "Incorrect number of chunks in chunk collection");
    }
}
