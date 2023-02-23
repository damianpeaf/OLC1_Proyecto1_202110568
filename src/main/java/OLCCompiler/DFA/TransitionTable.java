package OLCCompiler.DFA;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class TransitionTable {

    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    private NextTable nextTable;
    private int statesCounter;
    public State initialState;
    private Integer acceptanceNode;
    private Map<Integer, Object> tokens;


    public TransitionTable(NextTable nextTable, Set<Integer> initialStateNext, Integer acceptanceNode, Map<Integer, Object> tokens){
        this.transitions = new ArrayList<Transition>();
        this.states = new ArrayList<State>();
        this.nextTable = nextTable;
        this.statesCounter = -1;
        this.acceptanceNode = acceptanceNode;
        this.tokens = tokens;
        this.initialState = this.makeNode(initialStateNext);
    }

    private State makeNode(Set<Integer> nextSet){

        State state = this.evalCreateNewState(nextSet);

        if (state == null) {
            return null;
        }


        // FIND ASSOC TRANSITIONS WITH TOKENS

        //    token : nextSet
        Map<Object, Set<Integer>> assocTransitions = new HashMap<>();
        for (Integer next: nextSet) {
            if (next.equals(this.acceptanceNode)) {
                continue;
            }

            Object t = this.tokens.get(next);
            if(assocTransitions.containsKey(t)) {
                assocTransitions.get(t).addAll(new HashSet<Integer>(this.nextTable.getNext(next).next)); // CAN HAVE SOME ISSUES
            }else{
                assocTransitions.put(t, new HashSet<Integer>(this.nextTable.getNext(next).next));
            }
        }

        // CREATE TRANSITIONS
        for (Map.Entry<Object, Set<Integer>> entry: assocTransitions.entrySet()) {
            State nextState = this.evalCreateNewState(entry.getValue());
            if (nextState != null) {
                this.transitions.add(new Transition(state, nextState, entry.getKey()));
            }
        }

        // RECURSIVE CALL

        state.marked = true;

        for (State notMakedStates : this.states) {
            if (!notMakedStates.marked) {
                this.makeNode(notMakedStates.nextSet);
                break;
            }
        }

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
        this.states.add(newState);
        return newState;
    }

    public void printTransitions(){
        System.out.println("Transition Table");
        for (Transition t: this.transitions) {
            System.out.println(t.prevState.number + " -> " + t.nextState.number + " : " + t.token.toString());
        }
    }


    public void print(){

        // GET TERMINALS
        Set<String> terminals = new HashSet<>();
        for (Map.Entry<Integer, Object> entry: this.tokens.entrySet()) {

            if (entry.getKey().equals(this.acceptanceNode)) {
                continue;
            }

            terminals.add(entry.getValue().toString());
        }

        // PRINT ROW HEADERS

        System.out.print("State\t");

        for (String t: terminals) {
            System.out.print(t + "\t");
        }
        System.out.println();


        // PRINT TABLE

        for (State s: this.states) {
            System.out.print("S"+s.number + " " + s.nextSet + "\t\t");

            for (String t: terminals) {
                boolean found = false;
                for (Transition tr: this.transitions) {
                    if (tr.prevState.number == s.number && tr.token.toString().equals(t)) {
                        System.out.print("S"+tr.nextState.number + "\t");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print("\t");
                }
            }

            System.out.println();
        }

    }


    public void graphviz(){
        try (PrintWriter out = new PrintWriter(new File("src/test/java/OLCCompiler/automata.dot"))) {

            out.println("digraph automata {");
            out.println("rankdir=LR;");

            // STATES

            for (State s: this.states) {
                String shape = s.isAcceptace ? "doublecircle" : "circle";
                out.println(s.getGraphvizName() + " [label=\"" + s.getGraphvizName() + "\", shape=\""+shape+"\" ];");
            }

            // TRANSITIONS
            for (Transition t: this.transitions) {
                out.println(t.prevState.getGraphvizName() + " -> " + t.nextState.getGraphvizName() + " [label=\"" + t.token.toString() + "\"];");
            }

            out.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
