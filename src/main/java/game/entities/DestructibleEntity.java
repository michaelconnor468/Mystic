package game.entities;

public interface DestructibleEntity {
    public void damage( int health );
    public void heal( int health );
    public void onDestroy();
}