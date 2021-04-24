package game.main;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Stores and manages the current state of the game. Enforces game states to go through allowable transitions.
 * Used to also register lambdas to be called upon transition from one state to another.
 */
public class GameStateManager {
    public enum State {
        Initialization,
        MainMenu,
        Loading,
        Playing,
        Paused
    }
    private State state;
    private X x;
    private HashMap<State, HashSet<State>> canTransitionTo;
    private HashMap<State, HashMap<State, HashSet<GameStateChangeListener>>> onTransitionTo;
    private HashSet<GameStateChangeListener> onAnyTransition;

    private GameStateManager() {}
    public GameStateManager(X x) { 
        this.x = x;
        state = state.Initialization;
        canTransitionTo = new HashMap<State, HashSet<State>>();
        onTransitionTo = new HashMap<State, HashMap<State, HashSet<GameStateChangeListener>>>();
        onAnyTransition = new HashSet<GameStateChangeListener>();
        for ( State s : State.values() ) {
            canTransitionTo.put(s, new HashSet<State>());
            onTransitionTo.put(s, new HashMap<State, HashSet<GameStateChangeListener>>());
            for ( State to : State.values() )
                onTransitionTo.get(s).put(to, new HashSet<GameStateChangeListener>());
        }
        canTransitionTo.get(state.Initialization).add(state.MainMenu);

        canTransitionTo.get(state.MainMenu).add(state.Loading);

        canTransitionTo.get(state.Loading).add(state.Playing);

        canTransitionTo.get(state.Playing).add(state.Paused);

        canTransitionTo.get(state.Paused).add(state.MainMenu);
    }
    public State getState() { return state; }
    public boolean setState(State state) {
        if ( canTransitionTo.get(this.state).contains(state) ) {
            for( GameStateChangeListener listener : onAnyTransition )
                listener.beforeStateTransition(this.state, state);
            for( GameStateChangeListener listener : onTransitionTo.get(this.state).get(state) )
                listener.beforeStateTransition(this.state, state);

            State oldState = this.state;
            this.state = state;

            for( GameStateChangeListener listener : onAnyTransition )
                listener.afterStateTransition(oldState, state);
            for( GameStateChangeListener listener : onTransitionTo.get(this.state).get(state) )
                listener.afterStateTransition(oldState, state);
            return true;
        }
        else
            return false;
    }
    public void addGameStateChangeListener(GameStateChangeListener listener) {
        onAnyTransition.add(listener);
    }
    public void addGameStateChangeListener(State from, State to, GameStateChangeListener listener) {
        if ( canTransitionTo.get(from).contains(to) ) 
            onTransitionTo.get(from).get(to).add(listener);
    }
}
