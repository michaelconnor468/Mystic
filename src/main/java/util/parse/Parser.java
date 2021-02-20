package util.parse;

interface Parser<T> {
  // Helper method to help parsers keep count of how much text has been parsed
  
  public int getParsedLength();
  public T parse(String text);
}
