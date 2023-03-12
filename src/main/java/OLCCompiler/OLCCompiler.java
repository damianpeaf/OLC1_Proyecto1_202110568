package OLCCompiler;

import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Exception.EvaluationException;
import OLCCompiler.Exception.ParseException;
import OLCCompiler.Set.Set;
import OLCCompiler.Tree.Evaluation;
import OLCCompiler.Tree.EvaluationReport;
import OLCCompiler.Tree.RegexTree;
import OLCCompiler.Utils.ReportFileSystem;
import OLCCompiler.Utils.ReporthPaths;

import java.io.StringReader;
import java.util.ArrayList;

public class OLCCompiler {

    private OLCLexer lexer = null;
    private OLCParser parser = null;

    public ArrayList<RegexTree> generatedTrees = new ArrayList<RegexTree>();

    public String generateAutomatas(String entry, String filename) throws ParseException {
        // Report directories
        boolean folderCreated = ReportFileSystem.createReportsFolders(filename);
        if(!folderCreated){
            throw new ParseException("No se pudo crear el directorio de reportes.");
        }

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
                throw new ParseException("Hubo un error en la generación de los autómatas, al leer el archivo fuente. Ver reporte de errores.");
            }

            this.parser.errorTable.html("errores");
            this.generatedTrees = parser.regexTrees;
            return "Autómatas generados con éxito.";
        }catch (Exception e){
            this.parser.errorTable.html("errores");
            e.printStackTrace();
            throw new ParseException("Hubo un error en la generación de los autómatas, al leer el archivo fuente. Ver reporte de errores.");
        }
    }


    public String evalEntry(String entry, String filename) throws EvaluationException, ParseException {

        // Report directories
        boolean folderCreated = ReportFileSystem.createReportsFolders(filename);
        if(!folderCreated){
            throw new ParseException("No se pudo crear el directorio de reportes.");
        }

        if(this.parser == null || (this.parser != null && this.parser.regexTrees.size() == 0)){
            throw new EvaluationException("No se han generado los autómatas.");
        }

        // UPDATE AUTOMATAS ?
        try {
            this.generateAutomatas(entry, filename);
        } catch (ParseException e) {
            throw new EvaluationException("Hubo un error en la generación de los autómatas, al leer el archivo fuente. Ver reporte de errores.");
        }

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

    public String[] getRegexNames(){
        String[] names = new String[this.parser.regexTrees.size()];
        for (int i = 0; i < this.parser.regexTrees.size(); i++) {
            names[i] = this.parser.regexTrees.get(i).name;
        }
        return names;
    }

    public ReporthPaths getReportPaths(String regexName){

        for (RegexTree tree: this.parser.regexTrees) {
            if(tree.name.equals(regexName)){
                return tree.getReportPaths();
            }
        }
        return null;
    }
}


