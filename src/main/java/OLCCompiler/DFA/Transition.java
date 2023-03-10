package OLCCompiler.DFA;

import java.util.Set;

public class Transition {

    public State prevState;
    public State nextState;
    public Object token;

    public Transition (State prevState, State nextState, Object token){
        this.prevState = prevState;
        this.nextState = nextState;
        this.token = token;
    }

}
