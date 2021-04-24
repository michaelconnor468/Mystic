package init;

import game.main.Game;
import game.main.X;
import game.main.GameStateManager;
import game.main.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Initializer {
    private static X x;

    public static void main(String[] args) {
        x = new X();
        x.createGameSingleton(120);
        x.createGameStateManagerSingleton();
        Application.launch(App.class);
    }

    public static class App extends Application {
        public void start(Stage stage) {
            x.createWindowManagerSingleton(stage);
            x.getGameStateManager().setState(GameStateManager.State.MainMenu);
        }
    }
}
