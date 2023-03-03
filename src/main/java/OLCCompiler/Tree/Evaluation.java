package OLCCompiler.Tree;

import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Error.ErrorType;
import OLCCompiler.Error.OLCError;
import OLCCompiler.Tree.RegexTree;

import java.util.ArrayList;

public class Evaluation {

    public String evaluateString;
    public String regexName;
    public Boolean result = null;

    private ArrayList<RegexTree> treesTable;
    private RegexTree tree;
    private ErrorTable errorTable;

    public Evaluation(String regexName,String evaluateString, ArrayList<RegexTree> treesTable, ErrorTable errorTable) {
        this.evaluateString = evaluateString;
        this.regexName = regexName;
        this.treesTable = treesTable;
        this.errorTable = errorTable;
    }

    public void getTreeReference() {
        for (RegexTree t: treesTable) {
            if (t.name.equals(regexName)) {
                tree = t;
                return;
            }
        }
        errorTable.add(new OLCError(ErrorType.RUNTIME, "No se encontró la expresión regular '" + regexName +"'."));
    }

    public boolean evaluate() {
        this.result = tree.dfa.evalString(evaluateString);
        return this.result;
    }

    public String json() {
        return "\n{ \"Valor\": \"" + evaluateString + "\",\n  \"ExpresionRegular\": \"" + regexName + "\",\n \"Resultado\": \"" + (result ? "Cadena Válida" : "Cadena Inválida") + "\"\n}";
    }
}
