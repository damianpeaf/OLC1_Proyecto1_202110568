package OLCCompiler;

import OLCCompiler.Set.Set;
import OLCCompiler.Tree.RegexTree;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;

public class CompilerTests {

    @Test
    public void testAnalysis() throws Exception {
        File testFile = new File("src/test/java/OLCCompiler/test.olc");
        OLCLexer lexer = new OLCLexer(new FileReader(testFile));
        OLCParser parser = new OLCParser(lexer);

        try {
            parser.parse();

        }catch (Exception e){
            System.out.println(e.getMessage());

        }

        for (Set s: parser.setsTable.sets) {
            s.validate(parser.errorTable);
        }
        parser.setsTable.print();

        parser.errorTable.printErrors();

        System.out.println("REGEX DECLS: " + parser.regexTrees.size());
        System.out.println("EVA DECLS: " + parser.evaluationStrings.size());


        for (RegexTree treeReference: parser.regexTrees) {
            treeReference.make();
            treeReference.graphviz();
            treeReference.dfa.graphviz();
            treeReference.nextTable.print();
            treeReference.transitionTable.print();
            treeReference.ndfa.graphviz();
        }

    }

    @Test
    public void testAscii() throws Exception {

        int start = "!".charAt(0);
        int end = "}".charAt(0);

        System.out.println(start);
        System.out.println(end);

        for(int i = start; i <= end; i++){

            if((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)){
                continue;
            }

            System.out.println(Character.toString((char) i));
        }

    }

}
