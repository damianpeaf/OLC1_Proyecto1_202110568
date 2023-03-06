package OLCCompiler.Utils;

public class ReporthPaths {

    public String tree;
    public String nextTable;
    public String transitionsTable;
    public String dfa;
    public String ndfa;

    public ReporthPaths(String dfa, String ndfa, String tree, String nextTable, String transitionsTable) {
        this.dfa = dfa;
        this.ndfa = ndfa;
        this.tree = tree;
        this.nextTable = nextTable;
        this.transitionsTable = transitionsTable;
    }
}
