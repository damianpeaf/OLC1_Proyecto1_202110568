package OLCCompiler;

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
        parser.errorTable.printErrors();
        parser.setsTable.print();

        System.out.println("REGEX DECLS: " + parser.regexTrees.size());
        System.out.println("EVA DECLS: " + parser.evaluationStrings.size());

    }

}
