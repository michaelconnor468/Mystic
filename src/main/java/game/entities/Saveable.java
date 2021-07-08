package game.entities;

import util.parse.obj.ParserBlock;

public interface Saveable {
    /**
     * Objects are provided parserblocks to write the properties they want saved to for overloading convenience
     * and then return the block they saved their properties to in order to better inline saves.
     */
    public ParserBlock save(ParserBlock block);
}
