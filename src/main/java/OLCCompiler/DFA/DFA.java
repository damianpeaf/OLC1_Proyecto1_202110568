package OLCCompiler.DFA;

import OLCCompiler.Set.SetReference;
import OLCCompiler.Utils.Graphviz;
import OLCCompiler.Utils.ReportFileSystem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DFA {

    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    public String name;
    public String reportPath;

    public DFA(ArrayList<Transition> transitions, ArrayList<State> states, String name) {
        this.transitions = transitions;
        this.states = states;
        this.name = name;
        this.reportPath = ReportFileSystem.dfaReportPath + "/" + this.name +"_"+ReportFileSystem.filename;
    }

    public boolean evalString (String evalStr){

        // Get the initial state
        State currentState = this.states.get(0);

        for (char evalChar: evalStr.toCharArray()) {

            // Get the next state with the current eval char
            currentState = this.getNextState(currentState, evalChar);

            // If the next state is null, the string is not accepted
            if (currentState == null) return false;
        }

        // If the current state is an acceptance state (After reading the whole string), the string is accepted
        if (currentState.isAcceptace) return true;

        return false;
    }

    public State getNextState(State state, char evalChar){
        for (Transition t: this.transitions) {
            if (t.prevState == state){
                if(t.token instanceof String && t.token.toString().equals(String.valueOf(evalChar))) return t.nextState;
                if(t.token instanceof SetReference && ((SetReference) t.token).getSet().getElements().contains(String.valueOf(evalChar))) return t.nextState;
            }
        }
        return null;
    }

    public void graphviz() throws IOException {

        File dot = File.createTempFile("dfa", ".dot");
        dot.deleteOnExit();

        File image = new File(this.reportPath+".png");

        try (PrintWriter out = new PrintWriter(dot, "UTF-8")) {

            out.println("digraph automata {");
            out.println("rankdir=LR;");

            // STATES

            for (State s: this.states) {
                String shape = s.isAcceptace ? "doublecircle" : "circle";
                out.println(s.getGraphvizName() + " [label=\"" + s.getGraphvizName() + "\", shape=\""+shape+"\" ];");
            }

            // TRANSITIONS

            ArrayList<AbbreviateTransition> abbrTransitions = getAbbreviateTransitions();

            for (AbbreviateTransition t: abbrTransitions) {
                out.println(t.prevState.getGraphvizName() + " -> " + t.nextState.getGraphvizName() + " [label=\"" + t.getTokensString() + "\"];");
            }

            out.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Graphviz.generatePng(dot, image);
        this.reportPath = image.getAbsolutePath();
    }

    private ArrayList<AbbreviateTransition> getAbbreviateTransitions(){
        ArrayList<AbbreviateTransition> abbreviateTransitions = new ArrayList<>();

        for (Transition t: this.transitions) {
            boolean found = false;
            for (AbbreviateTransition at: abbreviateTransitions) {
                if (at.prevState.equals(t.prevState) && at.nextState.equals(t.nextState)){
                    at.addToken(t.token);
                    found = true;
                    break;
                }
            }
            if (!found) abbreviateTransitions.add(new AbbreviateTransition(t.prevState, t.nextState, t.token));
        }

        return abbreviateTransitions;
    }

}
