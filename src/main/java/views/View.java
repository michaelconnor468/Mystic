package views;

import javafx.scene.Scene;

/**
 * Abstranct interface for a view to be handled by WindowManager
 */
public interface View {
    public Scene deploy();
    public void recall();
}
