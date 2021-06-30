package game.main;

import util.parse.obj.*;
import views.*;

import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.stage.Stage;

public class WindowManager implements GameStateChangeListener {
    private Stage stage;
    private MainMenuView mainMenuView;
    private PlayingView playingView;
    private LoadingView loadingView;
    private PausedView pausedView;
    private SaveView saveView;
    // Do not use. Javafx does some initialization at canvas creation time for some reason so this is created.
    private Canvas _canvas; 
    private X x;

    private WindowManager() {}
    public WindowManager(X x, Stage stage) {
        this.stage = stage; 
        this._canvas = new Canvas(((ParserInt) x.getMainSettings().get("resolutionx")).getNumber(),
            ((ParserInt) x.getMainSettings().get("resolutiony")).getNumber());
        this.mainMenuView = new MainMenuView(x);
        this.playingView = new PlayingView(x);
        this.loadingView = new LoadingView(x);
        this.pausedView = new PausedView(x);
        this.saveView = new SaveView(x);
        this.x = x;
        stage.setTitle("Mystic");
        x.getGameStateManager().addGameStateChangeListener(this);
    }
    
    public void beforeStateTransition(GameStateManager.State from, GameStateManager.State to) {}
    public void afterStateTransition(GameStateManager.State from, GameStateManager.State to) {
        switch (to) {
            case MainMenu:
                stage.setScene(mainMenuView.deploy());
                stage.show();
                break;
            case Loading:
                stage.setScene(loadingView.deploy());
                break;
            case Playing:
                stage.setScene(playingView.deploy());
                break;
            case Paused:
                stage.setScene(pausedView.deploy());
                break;
            case Save:
                stage.setScene(saveView.deploy());
                break;
        }
        switch (from) {
            case MainMenu:
                mainMenuView.recall();
                break;
            case Loading:
                loadingView.recall();
                break;
            case Playing:
                playingView.recall();
                break;
            case Paused:
                pausedView.recall();
                break;
            case Save:
                saveView.recall();
                break;
        }
    }
}
