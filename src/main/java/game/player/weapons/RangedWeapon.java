package game.player.weapons;

import game.main.X;
import util.parse.obj.*;

public class RangedWeapon {
    private RangedWeapon() {}
    public static RangedWeapon load(X x, ParserBlock block) {
        RangedWeapon weapon = new RangedWeapon();
        return weapon;
    }
}
