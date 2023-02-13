package OLCCompiler.Tree;

import java.util.Set;

public class Node {

    public Node parent;
    public Node left = null;
    public Node right = null;
    public int number;
    public String type;
    public Object value;

    public Set<Integer> firstPos, lastPos;
    public boolean nullable;

    public Node (int number, String type, Object value){
        this.number = number;
        this.value = value;
    }

    public Node (int number, String type, Object value, Node left, Node right){
        this.number = number;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node (int number, String type, Object value, Node left){
        this.number = number;
        this.value = value;
        this.left = left;
        this.right = right;
    }
}
