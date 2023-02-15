package OLCCompiler.DFA;

import java.util.*;

public class TransitionTable {

    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    private NextTable nextTable;
    private int statesCounter;
    public State initialState;
    private Integer acceptanceNode;
    private Map<Integer, Set<String>> tokens;

    public TransitionTable(NextTable nextTable, Set<Integer> initialStateNext, Integer acceptanceNode, Map<Integer, Set<String>> tokens){
        this.transitions = new ArrayList<Transition>();
        this.states = new ArrayList<State>();
        this.nextTable = nextTable;
        this.statesCounter = -1;
        this.acceptanceNode = acceptanceNode;
        this.initialState = this.makeNode(initialStateNext);
        this.tokens = tokens;
    }

    private State makeNode(Set<Integer> nextSet){

        State state = this.evalCreateNewState(nextSet);

        if (state == null) {
            return null;
        }

        this.states.add(state);

        // FIND NEXT STATES
        // TODO : FIX THIS
        /*
        Map<String, Set<Integer>> assocTransitions = new HashMap<>();
        for (Integer next: nextSet) {

            assocTransitions.forEach((k,v) -> {
                if(this.tokens.get(next).contains(k)){
                    v.addAll(this.nextTable.getNext(next).next);
                }
            });



            Next n = this.nextTable.getNext(next);
            State nextState = this.makeNode(n.next);
            this.transitions.add(new Transition(state,nextState, n.token));
        }

         */

        return state;
    }

    private State evalCreateNewState(Set<Integer> nextSet){

        if (nextSet == null) {
            return null;
        }

        for (State state: this.states) {
            if (state.nextSet.equals(nextSet)) {
                return state;
            }
        }
        this.statesCounter++;
        State newState =  new State(this.statesCounter, nextSet);
        newState.setAcceptace(nextSet.contains(this.acceptanceNode));
        return newState;
    }

    public void print(){
        System.out.println("Transition Table");
        for (Transition t: this.transitions) {
            System.out.println(t.prevState.number + " -> " + t.nextState.number + " : " + t.tokens);
        }
    }


}
