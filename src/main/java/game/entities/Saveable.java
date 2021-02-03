package game.entities;
import java.nio.file.*;
import util.parse.obj.ParserBlock;

public interface Saveable {
  public void load(ParserBlock block);
  public ParserBlock save();
}
