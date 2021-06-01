package game.player.weapons;

import game.main.X;
import util.parse.obj.*;

public class MeleeWeapon {
    private MeleeWeapon() {}
    public static MeleeWeapon load(X x, ParserBlock block) {
        MeleeWeapon weapon = new MeleeWeapon();
        return weapon;
    }   
}
