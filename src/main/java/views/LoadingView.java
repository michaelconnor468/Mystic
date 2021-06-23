package views;

import views.scenes.LoadingScene;
import game.main.X;

import javafx.scene.Scene;

/**
 * Displayed during loading as a signifier. Has no dynamic functionality.
 */
public class LoadingView {
    private X x;

    private LoadingView() {}
    public LoadingView(X x) {
        this.x = x;
    }

    public Scene deploy() { return LoadingScene.getScene(x); }
    public void recall() {}
}
