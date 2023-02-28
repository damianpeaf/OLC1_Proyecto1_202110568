package OLCCompiler;

import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Tree.RegexTree;

import java.io.StringReader;

public class Compiler {

    private OLCLexer lexer = null;
    private OLCParser parser = null;

    public boolean generateAutomatas(String entry){
        try {
            this.lexer = new OLCLexer(new StringReader(entry));
            this.parser = new OLCParser(lexer);
            parser.parse();

            // TODO: Validate sets

            //TODO: validate regex refs

            for (RegexTree treeReference: parser.regexTrees) {
                treeReference.make();
                treeReference.graphviz();
                treeReference.dfa.graphviz();
                treeReference.ndfa.graphviz();
            }

            return true;
        }catch (Exception e){
            return false;
        }
    }


    public boolean evalEntry(String entry){

        if(this.parser == null || this.lexer == null){
            return false;
        }

        if(!this.generateAutomatas(entry)){
            return false;
        }

        //TODO: validate eval refs

        //TODO eval entry

        return true;
    }
    public ErrorTable getErrors(){
        return this.parser.errorTable;
    }
}
