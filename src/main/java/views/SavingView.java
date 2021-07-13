package views;

import views.scenes.LoadingScene;
import game.main.X;

import javafx.scene.Scene;

/**
 * Displayed during loading as a signifier. Has no dynamic functionality.
 */
public class SavingView {
    private X x;

    private SavingView() {}
    public SavingView(X x) {
        this.x = x;
    }

    public Scene deploy() { return LoadingScene.getScene(x, "Saving"); }
    public void recall() {}
}
