package game.player.weapons;

import game.main.X;
import game.player.Player;
import util.parse.obj.*;

public class Weapon {
    private X x;
    private Player player;

    protected Weapon() {}
    public static Weapon load(X x, Player player, ParserBlock block, Weapon weapon) {
        weapon.x = x;
        weapon.player = player;
        return weapon;
    }
}
