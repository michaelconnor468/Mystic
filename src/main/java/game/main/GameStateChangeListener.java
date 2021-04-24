package game.main;

import java.util.EventListener;

public interface GameStateChangeListener extends EventListener {
    public void beforeStateTransition(GameStateManager.State from, GameStateManager.State to);
    public void afterStateTransition(GameStateManager.State from, GameStateManager.State to);
}
