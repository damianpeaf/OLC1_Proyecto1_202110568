package OLCCompiler.DFA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TransitionTable {

    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    private NextTable nextTable;
    private int statesCounter;
    public State initialState;

    public TransitionTable(NextTable nextTable, Set<Integer> initialStateNext){
        this.transitions = new ArrayList<Transition>();
        this.states = new ArrayList<State>();
        this.nextTable = nextTable;
        this.statesCounter = -1;
        this.initialState = this.makeNode(initialStateNext);
    }

    private State makeNode(Set<Integer> nextSet){

        State state = this.evalCreateNewState(nextSet);
        this.states.add(state);

        if (nextSet == null) {
            return state;
        }

        // FIND NEXT STATES
        // TODO : FIX THIS
        for (Integer next: nextSet) {
            Next n = this.nextTable.getNext(next);
            State nextState = this.makeNode(n.next);
            this.transitions.add(new Transition(state,nextState, n.token));
        }

        return state;
    }

    private State evalCreateNewState(Set<Integer> nextSet){

        if (nextSet == null) {
            this.statesCounter++;
            State acceptanceState = new State(this.statesCounter, null);
            acceptanceState.setAcceptace(true);
            return acceptanceState;
        }

        for (State state: this.states) {
            if (state.nextSet.equals(nextSet)) {
                return state;
            }
        }
        this.statesCounter++;
        return new State(this.statesCounter, nextSet);
    }

    public void print(){
        System.out.println("Transition Table");
        for (Transition t: this.transitions) {
            System.out.println(t.prevState.number + " -> " + t.nextState.number + " : " + t.tokens);
        }
    }


}
