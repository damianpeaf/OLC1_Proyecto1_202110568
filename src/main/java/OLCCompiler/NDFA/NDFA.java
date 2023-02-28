package OLCCompiler.NDFA;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NDFA {

    public State initialState;
    public State finalState;
    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    private int stateCount = 0;
    public String name;

    private final String baseReportPath = "src/reports/AFND_202110568";

    public NDFA() {
        this.transitions = new ArrayList<>();
        this.states = new ArrayList<>();
    }

    public void addTransition(State from, State to, Object symbol) {
        this.transitions.add(new Transition(from, to, symbol));
    }

    public State addState() {
        State s = new State(stateCount);
        this.states.add(s);
        stateCount++;
        return s;
    }

    public void addState(State s) {
        s.number = stateCount;
        this.states.add(s);
        stateCount++;
    }

    public State addInitialState() {
        State s = this.addState();
        this.initialState = s;
        return s;
    }

    public State addFinalState() {
        State s = this.addState();
        this.finalState = s;
        return s;
    }

    public void addAllStates(ArrayList<State> states){
        for (State s : states) {
            this.addState(s);
        }
    }

    public void concat(NDFA left, NDFA right){

        this.initialState = left.initialState; // LEFT INIT is new INIT
        this.finalState = right.finalState; // RIGHT FINAL is new FINAL

        this.addAllStates(left.states); // ADD LEFT STATES

        // Exclude right initial state
        for(State s : right.states){
            if(!s.equals(right.initialState)){
                this.addState(s);
            }
        }

        // Add left transitions
        this.transitions.addAll(left.transitions);

        // Concat left final to right initial and the rest of right transitions
        for(Transition t : right.transitions){
            if(t.from.equals(right.initialState)) {
                this.addTransition(left.finalState, t.to, t.token);
            }else{
                this.addTransition(t.from, t.to, t.token);
            }
        }
    }

    public void union(NDFA left, NDFA right){

        this.initialState = this.addState(); // NEW INIT
        this.finalState = this.addState(); // NEW FINAL

        this.addTransition(this.initialState, left.initialState, new Epsilon()); // INIT -> LEFT INIT
        this.addTransition(this.initialState, right.initialState, new Epsilon()); // INIT -> RIGHT INIT

        this.addTransition(left.finalState, this.finalState, new Epsilon()); // LEFT FINAL -> FINAL
        this.addTransition(right.finalState, this.finalState, new Epsilon()); // RIGHT FINAL -> FINAL

        this.addAllStates(left.states); // ADD LEFT STATES
        this.addAllStates(right.states); // ADD RIGHT STATES

        // Add left transitions
        this.transitions.addAll(left.transitions);

        // Add right transitions
        this.transitions.addAll(right.transitions);
    }

    public void plus(NDFA ndfa){

        this.initialState = this.addInitialState(); // New init
        this.finalState = this.addFinalState(); // New final

        this.addAllStates(ndfa.states); // ADD NDFA STATES

        // Add all NDFA transitions
        this.transitions.addAll(ndfa.transitions);

        // new init to NDFA init
        this.addTransition(this.initialState, ndfa.initialState, new Epsilon());

        // NDFA final to NDFA init with epsilon
        this.addTransition(ndfa.finalState, ndfa.initialState, new Epsilon());

        // NDFA final to new final
        this.addTransition(ndfa.finalState, this.finalState, new Epsilon());
    }

    public void kleene(NDFA ndfa){
        this.initialState = this.addInitialState(); // New init
        this.finalState = this.addFinalState(); // New final

        this.addAllStates(ndfa.states); // ADD NDFA STATES

        // Add all NDFA transitions
        this.transitions.addAll(ndfa.transitions);

        // new init to NDFA init
        this.addTransition(this.initialState, ndfa.initialState, new Epsilon());

        // NDFA final to NDFA init with epsilon
        this.addTransition(ndfa.finalState, ndfa.initialState, new Epsilon());

        // NDFA final to new final
        this.addTransition(ndfa.finalState, this.finalState, new Epsilon());

        // new init to new final
        this.addTransition(this.initialState, this.finalState, new Epsilon());
    }

    public void optional(NDFA ndfa){

        this.initialState = this.addInitialState(); // New init
        this.finalState = this.addFinalState(); // New final

        this.addAllStates(ndfa.states); // ADD NDFA STATES

        // Add all NDFA transitions
        this.transitions.addAll(ndfa.transitions);

        // new init to NDFA init
        this.addTransition(this.initialState, ndfa.initialState, new Epsilon());

        // NDFA final to new final
        this.addTransition(ndfa.finalState, this.finalState, new Epsilon());

        // new init to new final
        this.addTransition(this.initialState, this.finalState, new Epsilon());
    }

    public void nodei(Object token){
        State initJoin = this.addInitialState();
        State finalJoin = this.addFinalState();

        this.addTransition(initJoin, finalJoin, token);
    }

    public void graphviz(){
        try (PrintWriter out = new PrintWriter(new File(this.baseReportPath + "/"+ this.name + ".dot"))) {

            out.println("digraph automata {");
            out.println("rankdir=LR;");

            // STATES

            for (State s: this.states) {
                String shape = s.isAcceptace ? "doublecircle" : "circle";
                out.println(s.getGraphvizName() + " [label=\"" + s.getGraphvizName() + "\", shape=\""+shape+"\" ];");
            }

            // TRANSITIONS
            for (Transition t: this.transitions) {
                out.println(t.from.getGraphvizName() + " -> " + t.to.getGraphvizName() + " [label=\"" + t.token.toString() + "\"];");
            }

            out.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
