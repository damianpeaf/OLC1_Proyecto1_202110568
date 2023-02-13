package OLCCompiler.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

public class Node {

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

    public void make(){
        //TODO: Make the tree
        makeAnullable(this);
        makePos(this);

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

    private void makeAnullable(Node node) {
        if (node != null) {

            // POSTORDER RECURSIVE CALLS
            if (node.left != null) {
                makeAnullable(node.left);
            }

            if (node.right != null) {
                makeAnullable(node.right);
            }

            // ANULABLE CONDITIONS
            if (node.type.equals(NodeType.NODE_I) || node.type.equals(NodeType.NODE_ACCEPT)){
                node.nullable = false;
            }else if (node.type.equals(NodeType.NODE_OR)){
                node.nullable = node.left.nullable || node.right.nullable;
            }else if (node.type.equals(NodeType.NODE_AND)){
                node.nullable = node.left.nullable && node.right.nullable;
            }else if(node.type.equals(NodeType.NODE_KLEENE)) {
                node.nullable = true;
            }else if(node.type.equals(NodeType.NODE_PLUS)) {
                node.nullable = node.left.nullable;
            }else if(node.type.equals(NodeType.NODE_OPTIONAL)) {
                node.nullable = true;
            }else{
                throw new RuntimeException("Node type not found for makeAnulable");
            }
        }
    }

    private void makePos(Node node) {
        if (node != null) {

            // POSTORDER RECURSIVE CALLS
            if (node.left != null) {
                makePos(node.left);
            }

            if (node.right != null) {
                makePos(node.right);
            }

            // POS CONDITIONS
            if (node.type.equals(NodeType.NODE_I) || node.type.equals(NodeType.NODE_ACCEPT)) {
                node.firstPos.add(node.number);
                node.lastPos.add(node.number);
            }else if (node.type.equals(NodeType.NODE_OR)) {
                node.firstPos.addAll(node.left.firstPos);
                node.firstPos.addAll(node.right.firstPos);
                node.lastPos.addAll(node.left.lastPos);
                node.lastPos.addAll(node.right.lastPos);
            }else if (node.type.equals(NodeType.NODE_AND)) {
                if (node.left.nullable) {
                    node.firstPos.addAll(node.left.firstPos);
                    node.firstPos.addAll(node.right.firstPos);
                } else {
                    node.firstPos.addAll(node.left.firstPos);
                }
                if (node.right.nullable) {
                    node.lastPos.addAll(node.left.lastPos);
                    node.lastPos.addAll(node.right.lastPos);
                } else {
                    node.lastPos.addAll(node.right.lastPos);
                }
            }else if (node.type.equals(NodeType.NODE_KLEENE) || node.type.equals(NodeType.NODE_PLUS) || node.type.equals(NodeType.NODE_OPTIONAL)) {
                node.firstPos.addAll(node.left.firstPos);
                node.lastPos.addAll(node.left.lastPos);
            }else{
                throw new RuntimeException("Node type not found for POS");
            }
        }
    }

    private void makeNext(Node node){
        if (node != null) {

            // POSTORDER RECURSIVE CALLS
            if (node.left != null) {
                makeNext(node.left);
            }

            if (node.right != null) {
                makeNext(node.right);
            }

            // NEXT CONDITIONS
            // ONLY FOR AND, PLUS AND KLEENE
            // TODO: RULES
        }
    }
}
