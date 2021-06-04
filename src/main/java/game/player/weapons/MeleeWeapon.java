package game.player.weapons;

import game.main.X;
import game.player.Player;
import util.parse.obj.*;

public class MeleeWeapon extends Weapon {
    public static MeleeWeapon load(X x, Player player, ParserBlock block, int id) {
        MeleeWeapon weapon = new MeleeWeapon();
        load(x, player, block, weapon, id);
        return weapon;
    }   

    public void use() { new MeleeAttack(x, player, id); }
}
