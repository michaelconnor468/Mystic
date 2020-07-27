package game.entities;

import java.util.LinkedList;
import game.main.timing.TickObserver;
import java.util.ArrayList;
import game.main.X;

public class FloorTile extends TileEntity {
    Biome biome;
    TileSpawnManager spawnManager;

    public void addSpawnableEntity(Entity entity, double probability, int ticksUntilNextSpawn) {
        //TODO
    }
    public void addSpawnOnStartupEntity(Entity entity, double probability) {
        //TODO
    }

    public void removeSpawnableEntity(Entity entity) {
        //TODO
    }
    public void removeSpawnOnStartupEntity(Entity entity) {
        //TODO
    }
    
    private class TileSpawnManager implements TickObserver {
        private ArrayList<Entity> spawnableEntities;
        private ArrayList<Entity> spawnOnStartup;
        private ArrayList<Double> spawnProbabilities;
        private ArrayList<Double> spawnOnStartupProbabilities;
        private ArrayList<Integer> ticksUntilNextSpawn;

        public void tick(X x) {

        }
    }
}