package OLCCompiler.NDFA;

public class State {

    public int number;
    public boolean isAcceptace = false;

    public State(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "State{" +
                "name='S" + number + '\'' +
                '}';
    }

    public String getGraphvizName(){
        return "S" + this.number;
    }

}
