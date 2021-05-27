package init;

import game.main.Game;
import game.main.X;
import game.main.GameStateManager;
import game.main.WindowManager;
import util.parse.FileParser;

import java.nio.file.Paths;
import java.nio.file.Path;

import javafx.application.Application;
import javafx.stage.Stage;

public class Initializer {
    private static X x;

    public static void main(String[] args) {
        x = new X();
        x.createSettingsSingletons(Paths.get("src/main/config/settings"));
        x.populateTemplates(Paths.get("src/main/config/templates"));
        x.createGameStateManagerSingleton();
        Application.launch(App.class);
    }

    public static void onExit() {
        System.exit(0);
    }

    public static class App extends Application {
        public void start(Stage stage) {
            x.createTimingManager();
            x.createWindowManagerSingleton(stage);
            x.createGameSingleton(120);
            x.createApplicationSingleton(this);
            x.getGameStateManager().setState(GameStateManager.State.MainMenu);
        }
        public void stop() { onExit(); }
    }
}
