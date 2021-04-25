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
        x.createGameStateManagerSingleton();
        x.createGameSingleton(120);
        Application.launch(App.class);
    }

    public static void onExit() {
        System.exit(0);
    }

    public static class App extends Application {
        public void start(Stage stage) {
            x.createWindowManagerSingleton(stage);
            x.createApplicationSingleton(this);
            x.getGameStateManager().setState(GameStateManager.State.MainMenu);
        }
        public void stop() { onExit(); }
    }
}
