package game.main;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Stores and manages the current state of the game. Enforces game states to go through allowable transitions.
 * Used to also register lambdas to be called upon transition from one state to another.
 */
public class GameStateManager {
    public enum State {
        MainMenu,
        Loading,
        Playing,
        Paused
    }
    private State state;
    private HashMap<State, HashSet<State>> canTransitionTo;
    private HashMap<State, HashMap<State, HashSet<GameStateChangeListener>>> onTransitionTo;

    public GameStateManager() { 
        state = state.MainMenu;
        canTransitionTo = new HashMap<State, HashSet<State>>();
        for ( State s : State.values() ) {
            canTransitionTo.put(s, new HashSet<State>());
            onTransitionTo.put(s, new HashMap<State, HashSet<GameStateChangeListener>>());
        }

        canTransitionTo.get(state.MainMenu).add(state.Loading);

        canTransitionTo.get(state.Loading).add(state.Playing);

        canTransitionTo.get(state.Playing).add(state.Paused);

        canTransitionTo.get(state.Paused).add(state.MainMenu);
    }
    public State getState() { return state; }
    public boolean setState(State state) {
        if ( canTransitionTo.get(this.state).contains(state) ) {
            for( GameStateChangeListener listener : onTransitionTo.get(this.state).get(state) )
                listener.beforeStateTransition(this.state, state);

            this.state = state;

            for( GameStateChangeListener listener : onTransitionTo.get(this.state).get(state) )
                listener.afterStateTransition(this.state, state);
            return true;
        }
        else
            return false;
    }
    public void addGameStateChangeListener(State from, State to, GameStateChangeListener listener) {
        if ( canTransitionTo.get(from).contains(to) ) {
            if ( onTransitionTo.get(from).get(to) == null )
                onTransitionTo.get(from).put(to, new HashSet<GameStateChangeListener>());
            onTransitionTo.get(from).get(to).add(listener);
        }
    }
}
