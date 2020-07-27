package game.entities;

public class Player extends DynamicEntity implements DestructibleEntity {
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
}