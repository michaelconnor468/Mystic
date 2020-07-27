package util.parse.obj;

public class ParserDouble implements ParserNumber {
    private double number;

    public ParserDouble( double number ) {this.number = number;}

    public double getNumber() { return number; }
    public String toJSON() { return Double.toString(number); }
    public ObjectType getType() { return ObjectType.DOUBLE; }
}