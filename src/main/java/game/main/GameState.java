package game.main;

public class GameState {
    public enum State {
        MainMenu,
        Playing,
        Paused
    }
    public State state;

    public GameState() { state = state.MainMenu; }
}
