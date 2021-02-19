package util.parse.obj;

/**
 * Used for parsing any generic numeric value of at most double in size. 
 * Recomended to use ParserInt for integer values to reduce user error.
 */
public class ParserDouble implements ParserNumber {
    private double number;

    public ParserDouble( double number ) {this.number = number;}

    public double getNumber() { return number; }
    public String toJSON() { return Double.toString(number); }
    public ObjectType getType() { return ObjectType.DOUBLE; }
    public String toString() { return toJSON(); }
}
