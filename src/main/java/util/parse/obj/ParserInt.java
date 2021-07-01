package util.parse.obj;

/**
 * Represents parsed integer value, exists for the purpose of eliminating frequent, possibly erroneuscasting 
 * by end user that would occur if only a generic number class were used
 */
public class ParserInt implements ParserNumber {
    private int number;

    public ParserInt( int number ) {this.number = number;}

    public int getNumber() {return number;}
    public ObjectType getType() { return ObjectType.INT; }
    public String toJSON() { return Integer.toString(number); }
    public String toString() { return Integer.toString(number); }
}
