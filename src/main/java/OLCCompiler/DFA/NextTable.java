package OLCCompiler.DFA;

import OLCCompiler.Utils.Graphviz;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class NextTable {

    public ArrayList<Next> nextTable;
    private final String baseReportPath = "src/reports/SIGUIENTES_202110568";
    public  String reportPath;
    private String name;

    public NextTable(String name){
        this.nextTable = new ArrayList<>();
        this.name = name;
        this.reportPath = this.baseReportPath + "/" + this.name;
    }

    public void addNext(Integer node, Object tokens, Set<Integer> next){
        for (Next n : nextTable){
            if (n.node.equals(node)){
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

    public  Integer getAcceptanceNode(){
        for (Next n: this.nextTable) {
            if (n.next == null) {
                return n.node;
            }
        }
        return null;
    }

    private void sort(){
        Comparator<Next> comparator = Comparator.comparingInt(o -> o.node);
        Collections.sort(this.nextTable, comparator);
    }

    public void graphviz() throws IOException {

        this.sort();

        File dot = File.createTempFile("next_table", ".dot");
        dot.deleteOnExit();

        File image = new File(this.reportPath + ".png");

        try (PrintWriter out = new PrintWriter(dot)) {
            out.write("graph G {");

            out.write("rankdir=LR;");
            out.write("node [shape = plaintext];");

            out.write("nextTable [");
            out.write("label = <");
            out.write("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">");

            out.write("<tr>");
            out.write("<td>Token</td>");
            out.write("<td>Numero de nodo</td>");
            out.write("<td>Siguientes</td>");
            out.write("</tr>");

            for(Next n: this.nextTable){
                out.write("<tr>");
                out.write("<td>" + n.getTokenString() + "</td>");
                out.write("<td>" + n.node + "</td>");
                out.write("<td>" + (n.next != null ? n.next : "-") + "</td>");
                out.write("</tr>");
            }

            out.write("</table>");
            out.write(">");
            out.write("];");

            out.write("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Graphviz.generatePng(dot, image);
        this.reportPath = image.getAbsolutePath();
    }

}
