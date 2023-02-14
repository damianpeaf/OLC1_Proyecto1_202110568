package OLCCompiler.DFA;

import java.util.Set;

public class Transition {

    public State prevState;
    public State nextState;
    public Set<String> tokens;

    public Transition (State prevState, State nextState, Set<String> tokens){
        this.prevState = prevState;
        this.nextState = nextState;
        this.tokens = tokens;
    }

}
