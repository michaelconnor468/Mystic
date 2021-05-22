package game.entities.buffs;

import util.parse.obj.*;

import java.util.HashMap;

public class Buff {
    private double maxHealthModifier;
    private double healthModifier;
    private double speedModifier;

    private Buff() {}

    public double getMaxHealthModifier() { return this.maxHealthModifier; }
    public double getHealthModifier() { return this.healthModifier; }
    public double getSpeedModifier() { return this.speedModifier; } 

    public static Buff load(ParserBlock block) {
        Buff buff = new Buff();
        HashMap<String, ParserObject> props = block.getProperties();

        buff.maxHealthModifier = props.containsKey("maxHealthModifier") ? 
            ((ParserDouble) props.get("maxHealthModifier")).getNumber() : 1;
        buff.healthModifier = props.containsKey("healthModifier") ? 
            ((ParserDouble) props.get("healthModifier")).getNumber() : 1;
        buff.speedModifier = props.containsKey("speedModifier") ? 
            ((ParserDouble) props.get("speedModifier")).getNumber() : 1;

        return buff;
    }
}
