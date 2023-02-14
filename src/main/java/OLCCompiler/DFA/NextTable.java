package OLCCompiler.DFA;

import java.util.ArrayList;
import java.util.Set;

public class NextTable {

    public ArrayList<Next> nextTable;

    public NextTable(){
        this.nextTable = new ArrayList<>();
    }

    /*
    @param node: node number
    @param token: Set of tokens (String)
    @param next: Set of next nodes (Integer)
     */
    public void addNext(Integer node, Set<String> tokens, Set<Integer> next){
        for (Next n : nextTable){
            if (n.node.equals(node) && n.token.equals(tokens)){
                n.addNext(next);
                return;
            }
        }
        nextTable.add(new Next(tokens, node, next));
    }

    public Next getNext(Integer node){
        for (Next posibleNext: this.nextTable) {

            if (posibleNext.node.equals(node)) {
                return posibleNext;
            }
        }
        throw new RuntimeException("Next node of " + node + " not found");
    }

    public void print(){
        System.out.println("Next Table");
        System.out.println("Numero de nodo | Token/s | Siguientes ");
        for (Next n: this.nextTable) {
            System.out.println(n.node + " | " + n.token + " | " + n.next);
        }
    }

}
