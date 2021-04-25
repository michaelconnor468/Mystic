package game.main;

import views.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager implements GameStateChangeListener {
    Stage stage;
    MainMenuView mainMenuView;
    X x;

    private WindowManager() {}
    public WindowManager(X x, Stage stage) {
        this.stage = stage; 
        this.mainMenuView = new MainMenuView(x, 1980, 1080);
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
        }
        switch (from) {
            case MainMenu:
                mainMenuView.recall();
                break;
        }
    }
}
