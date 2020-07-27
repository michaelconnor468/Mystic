package util.parse.obj;

public class ParserInt implements ParserNumber {
    private int number;

    public ParserInt( int number ) {this.number = number;}

    public int getNumber() {return number;}
}