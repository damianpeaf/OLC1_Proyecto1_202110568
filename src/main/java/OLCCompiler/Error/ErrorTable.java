package OLCCompiler.Error;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ErrorTable {

    public ArrayList<OLCError> errors;

    public ErrorTable() {
        errors = new ArrayList<OLCError>();
    }

    public void add (OLCError error) {
        errors.add(error);
    }

    public void printErrors () {
        for (OLCError error : errors) {
            System.out.println(error.getMessage() + " at line " + error.getLine() + " and column " + error.getColumn());
        }
    }

    public void html(String name) {

        try(PrintWriter out = new PrintWriter(new File("src/reports/ERRORES_202110568/" + name + ".html"))) {
            out.write("<html>");
            out.write("<head>");
            out.write("<title>ERRORES</title>");
            out.write("</head>");
            out.write("<body>");
            out.write("<h1>ERRORES</h1>");
            out.write("<table border='2'>");
            out.write("<tr>");
            out.write("<th>#</th>");
            out.write("<th>TIPO DE ERROR</th>");
            out.write("<th>DESCRIPCIÓN</th>");
            out.write("<th>LINEA</th>");
            out.write("<th>COLUMNA</th>");
            out.write("</tr>");

            int correlative = 1;
            for (OLCError error : errors) {
                out.write("<tr>");
                out.write("<td>" + (correlative++) + "</td>");
                out.write("<td>" + error.getType() + "</td>");
                out.write("<td>" + error.getMessage() + "</td>");
                out.write("<td>" + (error.getLine() != -1 ? error.getLine() +1 : "-") + "</td>");
                out.write("<td>" + (error.getColumn() != -1 ? error.getColumn() + 1 : "-") + "</td>");
                out.write("</tr>");
            }
            out.write("</table>");
            out.write("</body>");
            out.write("</html>");
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
