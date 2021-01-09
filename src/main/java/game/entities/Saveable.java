package game.entities;
import java.nio.file.*;

public interface Saveable {
  public void load(Path path);
  public void save(Path path);
}
