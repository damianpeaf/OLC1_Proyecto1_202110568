package OLCCompiler;

import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Set.Set;
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
                treeReference.graphviz();
                treeReference.dfa.graphviz();
                treeReference.ndfa.graphviz();
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


    public String evalEntry(String entry){

        if(this.parser == null || this.lexer == null){
            return "No se ha generado los autómatas.";
        }

        if(this.parser.errorTable.errors.size() > 0){
            return "No se han generado los autómatas correctamente. Ver reporte de errores.";
        }

        //TODO: validate eval refs

        //TODO eval entry

        return "Entrada evaluada con éxito.";
    }
    public ErrorTable getErrors(){
        return this.parser.errorTable;
    }
}
