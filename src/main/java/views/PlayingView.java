package views;

import game.main.X;
import views.scenes.PlayingScene;

import javafx.scene.Scene;

public class PlayingView implements View {
    private X x;

    private PlayingView() {}
    public PlayingView(X x) {
        this.x = x;
    }

    public Scene deploy() { return PlayingScene.getScene(x); }
    public void recall() {}
}
