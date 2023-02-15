package OLCCompiler.Tree;

import OLCCompiler.DFA.NextTable;
import OLCCompiler.DFA.TransitionTable;
import OLCCompiler.Set.SetReference;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Node {

    public Node left = null;
    public Node right = null;
    public int number;
    public String type;
    public Object value;

    public boolean nullable;
    public Set<Integer> firstPos = new HashSet();
    public Set<Integer> lastPos = new HashSet();

    public NextTable nextTable;
    public TransitionTable transitionTable;
    public Map<Integer, Set<Object>> tokens;

    public Node(int number, String type, Object value) {
        this.number = number;
        this.value = value;
        this.type = type;
    }

    public Node(int number, String type, Object value, Node left, Node right) {
        this.number = number;
        this.value = value;
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Node(int number, String type, Object value, Node left) {
        this.number = number;
        this.value = value;
        this.left = left;
        this.type = type;
    }


    public void graphviz(String path) {
        try (PrintWriter out = new PrintWriter(new File(path))) {
            out.write("graph G {");
            declareGraphvizNodes(this, out);
            generateGraphvizNodesRelations(this, out);
            out.write("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void make() {

        this.nextTable = new NextTable();
        this.tokens = new HashMap<Integer, Set<Object>>();

        makeAnullable(this);
        makePos(this);
        makeTokens(this);
        System.out.println("Tokens: " + this.tokens);
        makeNext(this);

        //this.transitionTable = new TransitionTable(this.nextTable, this.firstPos, this.nextTable.getAcceptanceNode(), this.tokens);

    }

    private void declareGraphvizNodes(Node node, PrintWriter out) {
        if (node != null) {
            out.write(node.getGraphvizNode() + " [label=\"" + node.getGraphvizLabel() + "\", shape=\"circle\", width=1, height=1];");
            out.write("\n");
            declareGraphvizNodes(node.left, out);
            declareGraphvizNodes(node.right, out);
        }
    }

    private void generateGraphvizNodesRelations(Node node, PrintWriter out) {
        if (node != null) {
            if (node.left != null) {
                out.write(node.getGraphvizNode() + " -- " + node.left.getGraphvizNode() + ";");
                out.write("\n");
                generateGraphvizNodesRelations(node.left, out);
            }
            if (node.right != null) {
                out.write(node.getGraphvizNode() + " -- " + node.right.getGraphvizNode() + ";");
                out.write("\n");
                generateGraphvizNodesRelations(node.right, out);
            }
        }
    }

    private String getGraphvizNode() {
        switch (this.type){
            case NodeType.NODE_I:
                return "I_" + this.number;
            case NodeType.NODE_ACCEPT:
                return "I_ACEPTANCE_" + this.number;
            case NodeType.NODE_PLUS:
                return "PLUS_" + this.number;
            case NodeType.NODE_OR:
                return "OR_" + this.number;
            case NodeType.NODE_KLEENE:
                return "KLEENE_" + this.number;
            case NodeType.NODE_OPTIONAL:
                return "OPTIONAL_" + this.number;
            case NodeType.NODE_AND:
                return "AND_" + this.number;
            default:
                return "NODE_" + this.number;
        }
    }

    private void makeTokens(Node node) {
        if (node != null) {
            if (node.type.equals(NodeType.NODE_I) || node.type.equals(NodeType.NODE_ACCEPT)) {
                if (node.value instanceof String) {
                    this.tokens.put(node.number, new HashSet<Object>() {{
                        add(node.value.toString());
                    }});
                } else if (node.value instanceof SetReference) {
                    //this.tokens.put(node.number, ((SetReference) node.value).); // <- GET SET ELEMENTS FROM SETREFERENCE
                    this.tokens.put(node.number, new HashSet<Object>() {{
                        add(
                                ((SetReference) node.value)
                        );
                    }});
                }
            }
            if (node.left != null) {
                makeTokens(node.left);
            }
            if (node.right != null) {
                makeTokens(node.right);
            }
        }
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
            if (node.type.equals(NodeType.NODE_I) || node.type.equals(NodeType.NODE_ACCEPT)) {
                node.nullable = false;
            } else if (node.type.equals(NodeType.NODE_OR)) {
                node.nullable = node.left.nullable || node.right.nullable;
            } else if (node.type.equals(NodeType.NODE_AND)) {
                node.nullable = node.left.nullable && node.right.nullable;
            } else if (node.type.equals(NodeType.NODE_KLEENE)) {
                node.nullable = true;
            } else if (node.type.equals(NodeType.NODE_PLUS)) {
                node.nullable = node.left.nullable;
            } else if (node.type.equals(NodeType.NODE_OPTIONAL)) {
                node.nullable = true;
            } else {
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
            if (node.type.equals(NodeType.NODE_I)) {
                node.firstPos.add(node.number);
                node.lastPos.add(node.number);
            } else if (node.type.equals(NodeType.NODE_OR)) {
                node.firstPos.addAll(node.left.firstPos);
                node.firstPos.addAll(node.right.firstPos);
                node.lastPos.addAll(node.left.lastPos);
                node.lastPos.addAll(node.right.lastPos);
            } else if (node.type.equals(NodeType.NODE_AND)) {
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
            } else if (node.type.equals(NodeType.NODE_KLEENE) || node.type.equals(NodeType.NODE_PLUS) || node.type.equals(NodeType.NODE_OPTIONAL)) {
                node.firstPos.addAll(node.left.firstPos);
                node.lastPos.addAll(node.left.lastPos);
            }else if (node.type.equals(NodeType.NODE_ACCEPT)) {
                node.firstPos.add(node.number);
                node.lastPos.add(node.number);
            }else {
                throw new RuntimeException("Node type not found for POS");
            }
        }
    }

    private void makeNext(Node node) {
        if (node != null) {

            // NEXT CONDITIONS, ONLY FOR AND, PLUS AND KLEENE
            if (node.type.equals(NodeType.NODE_AND) && (node.left != null) && (node.right != null)) {
                for (Integer i : node.left.lastPos) {
                    this.nextTable.addNext(i, this.tokens.get(i), new HashSet<>(node.right.firstPos));
                }
            }

            if ((node.type.equals(NodeType.NODE_KLEENE) || node.type.equals(NodeType.NODE_PLUS)) && (node.left != null)) {
                for (Integer i : node.left.lastPos) {
                    this.nextTable.addNext(i, this.tokens.get(i), node.left.firstPos);
                }
            }

            if (node.type.equals(NodeType.NODE_ACCEPT)) {
                this.nextTable.addNext(node.number, this.tokens.get(node.number), null);
            }

            // POSTORDER RECURSIVE CALLS
            if (node.left != null) {
                makeNext(node.left);
            }

            if (node.right != null) {
                makeNext(node.right);
            }

        }
    }

    private String getGraphvizLabel() {
        if (this.type.equals(NodeType.NODE_PLUS) || this.type.equals(NodeType.NODE_KLEENE) || this.type.equals(NodeType.NODE_OPTIONAL) || this.type.equals(NodeType.NODE_OR) || this.type.equals(NodeType.NODE_AND)) {
            return "Anulable: " + (this.nullable ? "V" : "F") + "\n" + "FirstPos: " + this.firstPos + "\n" + "LastPos: " + this.lastPos + "\n"+ this.value.toString();
        }else{
            return "Anulable: " + (this.nullable ? "V" : "F") + "\n" + "FirstPos: " + this.firstPos + "\n" + "LastPos: " + this.lastPos + "\n" + "Node: " + this.number + "\n"+ this.value.toString();
        }
    }
}
