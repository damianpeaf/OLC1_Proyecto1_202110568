package OLCCompiler.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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


    public void graphviz(String path) {
        try(PrintWriter out = new PrintWriter(new File(path))) {
            out.write("graph G {");
            declareGraphvizNodes(this, out);
            generateGraphvizNodesRelations(this, out);
            out.write("}");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void declareGraphvizNodes(Node node, PrintWriter out) {
        if (node != null){
            out.write(node.getGraphvizNode() + " [label=\"" + node.value.toString() + "\", shape=\"circle\", width=1, height=1];");
            out.write("\n");
            declareGraphvizNodes(node.left, out);
            declareGraphvizNodes(node.right, out);
        }
    }

    private void generateGraphvizNodesRelations(Node node, PrintWriter out) {
        if (node != null){
            if (node.left != null) {
                out.write(node.getGraphvizNode() + " -- " + node.left.getGraphvizNode() +";" );
                out.write("\n");
                generateGraphvizNodesRelations(node.left, out);
            }
            if (node.right != null) {
                out.write(node.getGraphvizNode() + " -- " + node.right.getGraphvizNode() +";" );
                out.write("\n");
                generateGraphvizNodesRelations(node.right, out);
            }
        }
    }

    private String getGraphvizNode(){
        return this.type+"_"+this.number;
    }
}
