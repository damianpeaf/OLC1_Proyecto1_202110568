package OLCCompiler.NDFA;

public class Transition {

    public State from;
    public State to;
    public Object token;

    public Transition(State from, State to, Object token) {
        this.from = from;
        this.to = to;
        this.token = token;
    }

}
