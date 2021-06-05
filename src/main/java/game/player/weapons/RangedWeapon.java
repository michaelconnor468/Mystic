package game.player.weapons;

import game.main.X;
import game.player.Player;
import util.parse.obj.*;

public class RangedWeapon extends Weapon {
    private RangedWeapon() {}
    public static RangedWeapon load(X x, Player player, ParserBlock block) {
        RangedWeapon weapon = new RangedWeapon();
        load(x, player, block, weapon);
        return weapon;
    }
}
