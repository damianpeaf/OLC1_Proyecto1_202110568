package OLCCompiler.DFA;

import OLCCompiler.Utils.Graphviz;
import OLCCompiler.Utils.ReportFileSystem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TransitionTable {

    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    private NextTable nextTable;
    private int statesCounter;
    public State initialState;
    private Integer acceptanceNode;
    public Map<Integer, Object> tokens;
    private ArrayList<String> terminals;
    
    public String reportPath;
    private String name;

    public TransitionTable(NextTable nextTable, Set<Integer> initialStateNext, Integer acceptanceNode, Map<Integer, Object> tokens, ArrayList<String> terminals, String name){
        this.transitions = new ArrayList<Transition>();
        this.states = new ArrayList<State>();
        this.nextTable = nextTable;
        this.statesCounter = -1;
        this.acceptanceNode = acceptanceNode;
        this.tokens = tokens;
        this.terminals = terminals;
        this.initialState = this.makeNode(initialStateNext);

        this.name = name;
        this.reportPath = ReportFileSystem.transitionTableReportPath + "/" + this.name + "_"+ReportFileSystem.filename;
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
                assocTransitions.get(t).addAll(new HashSet<Integer>(this.nextTable.getNext(next).next));
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

    public void graphviz () throws IOException {

        File dot = File.createTempFile("transition_table", ".dot");
        dot.deleteOnExit();

        File image = new File(this.reportPath + ".png");

        try (PrintWriter out = new PrintWriter(dot, "UTF-8")) {
            out.write("graph G {");

            out.write("rankdir=LR;");
            out.write("node [shape = plaintext];");

            out.write("nextTable [");
            out.write("label = <");
            out.write("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">");

            // PRINT ROW HEADERS
            out.write("<tr>");
            out.write("<td>Estados</td>");
            for (String t: this.terminals) {

                if(t.equals(" ")){
                    t = "\" \"";
                }else if(t.equals("\n")){
                    t = "\\\\n";
                }

                out.write("<td>"+t+"</td>");
            }
            out.write("</tr>");

            for (State s: this.states) {
                out.write("<tr>");
                out.write("<td>S"+s.number + " " + s.nextSet + "</td>");

                for (String t: this.terminals) {
                    boolean found = false;
                    for (Transition tr: this.transitions) {
                        if (tr.prevState.number == s.number && tr.token.toString().equals(t)) {
                            out.write("<td>S"+tr.nextState.number + "</td>");
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        out.write("<td></td>");
                    }
                }

                out.write("</tr>");
            }

            out.write("</table>");
            out.write(">");
            out.write("];");

            out.write("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Graphviz.generatePng(dot, image);
        this.reportPath = image.getAbsolutePath();

    }

}
