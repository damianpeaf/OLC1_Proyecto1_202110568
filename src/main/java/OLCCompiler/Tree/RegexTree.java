package OLCCompiler.Tree;

import OLCCompiler.DFA.DFA;
import OLCCompiler.DFA.NextTable;
import OLCCompiler.DFA.TransitionTable;
import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Error.ErrorType;
import OLCCompiler.Error.OLCError;
import OLCCompiler.NDFA.NDFA;
import OLCCompiler.Set.SetReference;
import OLCCompiler.Utils.Graphviz;
import OLCCompiler.Utils.ReportFileSystem;
import OLCCompiler.Utils.ReporthPaths;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RegexTree {

    public String name;
    public Node rootNode;
    public String reportPath;
    private ErrorTable errorTable;

    // TREE METHOD VARIABLES
    public ArrayList<String> terminals;
    public Map<Integer, Object> tokens;
    public NextTable nextTable;
    public TransitionTable transitionTable;
    public DFA dfa;

    // THOMPSON METHOD VARIABLES
    public NDFA ndfa;


    public RegexTree(String name, Node rootNode, ErrorTable errorTable) {
        this.name = name;
        this.rootNode = rootNode;
        this.errorTable = errorTable;
    }

    public void graphviz() throws IOException {

        this.reportPath = ReportFileSystem.treeReportPath + "/" + this.name + "_" + ReportFileSystem.filename;

        File dot = File.createTempFile("tree", ".dot");
        dot.deleteOnExit();

        File image = new File(this.reportPath +".png");
        try (PrintWriter out = new PrintWriter(dot, "UTF-8")) {
            out.write("graph G {");
            declareGraphvizNodes(this.rootNode, out);
            generateGraphvizNodesRelations(this.rootNode, out);
            out.write("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Graphviz.generatePng(dot, image);
        this.reportPath = image.getAbsolutePath();
    }

    public void make() {

        this.nextTable = new NextTable(this.name);
        this.tokens = new HashMap<Integer, Object>();
        this.terminals = new ArrayList<String>();

        makeAnullable(this.rootNode);
        makePos(this.rootNode);
        makeTokens(this.rootNode);
        makeNext(this.rootNode);

        this.transitionTable = new TransitionTable(this.nextTable, this.rootNode.firstPos, this.nextTable.getAcceptanceNode(), this.tokens, this.terminals, this.name);
        this.dfa = new DFA(this.transitionTable.transitions, this.transitionTable.states, this.name);

        this.ndfa = this.makeThompson(this.rootNode.left);
        this.ndfa.finalState.isAcceptace = true;
        this.ndfa.name = this.name;

        this.generateReports();
    }

    private void generateReports(){
        try {
            this.graphviz();
            this.nextTable.graphviz();
            this.transitionTable.graphviz();
            this.dfa.graphviz();
            this.ndfa.graphviz();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void declareGraphvizNodes(Node node, PrintWriter out) {
        if (node != null) {
            out.write(node.getGraphvizNode() + " [label=" + node.getGraphvizLabel() + ", shape=\"circle\", width=1, height=1, fontsize=14, margin=0];");
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

    private void makeTokens(Node node) {
        if (node != null) {
            if (node.type.equals(NodeType.NODE_I) || node.type.equals(NodeType.NODE_ACCEPT)) {
                if (node.value instanceof String) {

                    if(((String) node.value).length() != 1){
                        this.errorTable.add(new OLCError(ErrorType.RUNTIME, "La longitud del token \"" + node.value + "\" debe ser de 1 para la Regex " + this.name + "."));
                    }

                    if(!node.type.equals(NodeType.NODE_ACCEPT) && !this.terminals.contains(node.value.toString())){
                        this.terminals.add((String) node.value);
                    }

                    this.tokens.put(node.number, node.value.toString());
                } else if (node.value instanceof SetReference) {

                    // CHECK SET REFERENCE
                    OLCCompiler.Set.Set set = ((SetReference) node.value).getSet();
                    if (set == null){
                        this.errorTable.add(new OLCError(ErrorType.RUNTIME, "No se encontró la referencia al conjunto {" + ((SetReference) node.value).name + "} para la Regex " + this.name + "."));
                    }

                    this.tokens.put(node.number, node.value);
                    // Add to terminal once
                    if(!this.terminals.contains(node.value.toString())){
                        this.terminals.add(node.value.toString());
                    }
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

            // ? POSTORDER RECURSIVE CALLS
            if (node.left != null) {
                makeNext(node.left);
            }

            if (node.right != null) {
                makeNext(node.right);
            }

        }
    }


    private NDFA makeThompson(Node node) {

        // POST-ORDER RECURSIVE CALLS

        NDFA mainNdfa= new NDFA();
        NDFA leftNdfa = null;
        NDFA rightNdfa = null;

        if (node.left != null) {
            leftNdfa = makeThompson(node.left);
        }

        if (node.right != null) {
            rightNdfa = makeThompson(node.right);
        }

        // THOMPSON CONDITIONS

        if (node.type.equals(NodeType.NODE_I)) {
            mainNdfa.nodei(this.tokens.get(node.number));
            return mainNdfa;
        }else if (node.type.equals(NodeType.NODE_AND)) {
            mainNdfa.concat(leftNdfa, rightNdfa);
            return mainNdfa;
        }else if(node.type.equals(NodeType.NODE_OR)) {
            mainNdfa.union(leftNdfa, rightNdfa);
            return mainNdfa;
        }else if(node.type.equals(NodeType.NODE_KLEENE)) {
            mainNdfa.kleene(leftNdfa);
            return mainNdfa;
        }else if(node.type.equals(NodeType.NODE_PLUS)) {
            mainNdfa.plus(leftNdfa);
            return mainNdfa;
        }else if(node.type.equals(NodeType.NODE_OPTIONAL)) {
            mainNdfa.optional(leftNdfa);
            return mainNdfa;
        }

        return null;
    }

    public ReporthPaths getReportPaths() {
        return new ReporthPaths(this.dfa.reportPath, this.ndfa.reporthPath, this.reportPath, this.nextTable.reportPath, this.transitionTable.reportPath);
    }

}
