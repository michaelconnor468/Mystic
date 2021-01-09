package game.entities;

import util.parse.*;
import util.parse.obj.*;
import java.nio.file.*;

public class Player extends DynamicEntity implements DestructibleEntity, Saveable {
    int health;
    int maxHealth;
    
    public void damage( int health ) {
        this.health -= health;
        if ( this.health <= 0 )
            onDestroy();
    }

    public void heal( int health ) {
        this.health += health;
        if ( this.health > maxHealth )
            this.health = maxHealth;
    }

    public void onDestroy() {

    }

    public void load( Path path ) {
          
    }

    public void save( Path path ) {

    }
}
