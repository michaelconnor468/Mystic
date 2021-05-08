package game.main;

import views.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager implements GameStateChangeListener {
    Stage stage;
    MainMenuView mainMenuView;
    PlayingView playingView;
    X x;

    private WindowManager() {}
    public WindowManager(X x, Stage stage) {
        this.stage = stage; 
        this.mainMenuView = new MainMenuView(x);
        this.playingView = new PlayingView(x);
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
                break;
            case Playing:
                stage.setScene(playingView.deploy());
                break;
        }
        switch (from) {
            case MainMenu:
                mainMenuView.recall();
                break;
            case Loading:    
                break;
            case Playing:
                playingView.recall();
                break;
        }
    }
}
