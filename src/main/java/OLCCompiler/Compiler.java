package OLCCompiler;

import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Exception.EvaluationException;
import OLCCompiler.Set.Set;
import OLCCompiler.Tree.Evaluation;
import OLCCompiler.Tree.EvaluationReport;
import OLCCompiler.Tree.RegexTree;

import java.io.StringReader;

public class Compiler {

    private OLCLexer lexer = null;
    private OLCParser parser = null;

    public String generateAutomatas(String entry){
        try {
            this.lexer = new OLCLexer(new StringReader(entry));
            this.parser = new OLCParser(lexer);
            parser.parse();

            // Validate Sets
            for (Set set: parser.setsTable.sets) {
                set.validate(parser.errorTable);
            }

            for (RegexTree treeReference: parser.regexTrees) {
                treeReference.make();
            }

            if(parser.errorTable.errors.size() > 0){
                this.parser.errorTable.html("errores");
                return "Hubo un error en la generación de los autómatas, durante la ejecución. Ver reporte de errores.";
            }

            this.parser.errorTable.html("errores");
            return "Autómatas generados con éxito.";
        }catch (Exception e){
            this.parser.errorTable.html("errores");
            return "Hubo un error en la generación de los autómatas, al leer el archivo fuente. Ver reporte de errores.";
        }
    }


    public String evalEntry(String entry) throws EvaluationException {

        if(this.parser == null || (this.parser != null && this.parser.regexTrees.size() == 0)){
            throw new EvaluationException("No se han generado los autómatas.");
        }

        // UPDATE AUTOMATAS ?
        this.generateAutomatas(entry);

        for (Evaluation e: parser.evaluationStrings) {
            e.getTreeReference();
        }

        if(parser.errorTable.errors.size() > 0){
            this.parser.errorTable.html("errores");
            return "Hubo errores durante la ejecución. Ver reporte de errores.";
        }

        EvaluationReport report = new EvaluationReport();
        for (Evaluation e: parser.evaluationStrings) {
            e.evaluate();
            report.add(e);
        }

        report.json("salida");

        this.parser.errorTable.html("errores");
        return "Entrada evaluada con éxito.";
    }
    public ErrorTable getErrors(){
        return this.parser.errorTable;
    }
}


