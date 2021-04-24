package init;

import game.main.Game;
import game.main.X;

public class Initializer {
    public static void main(String[] args) {
        X x = new X();
        x.createGameSingleton(120);
    }
}
