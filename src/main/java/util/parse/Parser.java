package util.parse;

public interface Parser<T> {
  // Helper method to help parsers keep count of how much text has been parsed
  
  // TODO implement this in all parsers
  //public int getParsedLength();
  public T parse(String text);
}
