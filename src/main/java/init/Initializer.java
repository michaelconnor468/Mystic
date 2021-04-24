package init;

import game.main.Game;
import game.main.X;

public class Initializer {
    public static void main(String[] args) {
        Game game = new Game(new X(), 120);
        game.start();
    }
}
