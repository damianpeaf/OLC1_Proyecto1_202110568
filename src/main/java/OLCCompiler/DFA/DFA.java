package OLCCompiler.DFA;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DFA {

    public ArrayList<Transition> transitions;
    public ArrayList<State> states;
    public String name;
    private final String baseReportPath = "src/reports/AFD_202110568";
    private final String reportPath;

    public DFA(ArrayList<Transition> transitions, ArrayList<State> states, String name) {
        this.transitions = transitions;
        this.states = states;
        this.name = name;
        this.reportPath = this.baseReportPath + "/" + this.name;
    }

    public boolean evalString (String evalStr){
        // TODO: Implement

        return false;
    }

    public void graphviz(){
        try (PrintWriter out = new PrintWriter(new File(this.reportPath + ".dot"))) {

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
