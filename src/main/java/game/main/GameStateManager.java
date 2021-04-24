package game.main;

import java.util.HashMap;
import java.util.HashSet;

public class GameStateManager {
    public enum State {
        MainMenu,
        Loading,
        Playing,
        Paused
    }
    private State state;
    private HashMap<State, HashSet<State>> canTransitionTo;
    // TODO populate with state change listeners
    private HashMap<State, HashSet<Object>> onTransitionTo;

    public GameStateManager() { 
        state = state.MainMenu;
        canTransitionTo = new HashMap<State, HashSet<State>>();
        for ( State s : State.values() )
            canTransitionTo.put(s, new HashSet<State>());

        canTransitionTo.get(state.MainMenu).add(state.Loading);

        canTransitionTo.get(state.Loading).add(state.Playing);

        canTransitionTo.get(state.Playing).add(state.Paused);

        canTransitionTo.get(state.Paused).add(state.MainMenu);
    }
    public State getState() { return state; }
    public boolean setState(State state) {
        if ( canTransitionTo.get(this.state).contains(state) ) {
            this.state = state;
            return true;
        }
        else
            return false;
    }
}
