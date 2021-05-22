package game.entities.buffs;

import util.parse.obj.*;
import game.main.TickObserver;
import game.entities.Entity;
import game.main.X;

import java.util.HashMap;

public class Buff implements TickObserver {
    private double maxHealthModifier;
    private double healthModifier;
    private double speedModifier;
    private Entity entity;
    private X x;
    private String name;
    private int ticksToLive;

    private Buff() {}

    public double getMaxHealthModifier() { return this.maxHealthModifier; }
    public double getHealthModifier() { return this.healthModifier; }
    public double getSpeedModifier() { return this.speedModifier; } 
    public String getName() { return this.name; }

    public static Buff load(X x, Entity entity, String name, ParserBlock block) {
        Buff buff = new Buff();
        HashMap<String, ParserObject> props = block.getProperties();

        buff.x = x;
        buff.entity = entity;
        
        buff.name = ((ParserString) props.get("name")).getString();
        buff.maxHealthModifier = props.containsKey("maxHealthModifier") ? 
            ((ParserDouble) props.get("maxHealthModifier")).getNumber() : 1;
        buff.healthModifier = props.containsKey("healthModifier") ? 
            ((ParserDouble) props.get("healthModifier")).getNumber() : 1;
        buff.speedModifier = props.containsKey("speedModifier") ? 
            ((ParserDouble) props.get("speedModifier")).getNumber() : 1;
        double secondsToLive = props.containsKey("seconds") ? 
            ((ParserDouble) props.get("seconds")).getNumber() : 1;
        buff.ticksToLive = (int) secondsToLive*x.getTimingManager().getTicksPerSecond();

        return buff;
    }

    public void tick(X x) {
        if ( --ticksToLive < 1 )
            entity.removeBuff(this);
    }
}
