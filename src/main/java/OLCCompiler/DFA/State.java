package OLCCompiler.DFA;


import java.util.Set;

public class State {

    public int number;
    public boolean isAcceptace = false;
    public Set<Integer> nextSet;
    public boolean marked = false;

    public State(int number, Set<Integer> nextSet){
        this.number = number;
        this.nextSet = nextSet;
    }

    public void setAcceptace(boolean acceptace) {
        isAcceptace = acceptace;
    }
}
