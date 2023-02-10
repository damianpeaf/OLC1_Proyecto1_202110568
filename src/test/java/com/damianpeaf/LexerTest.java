package com.damianpeaf;

import java_cup.runtime.Symbol;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import OLCCompiler.*;

public class LexerTest {

    @Test
    public void testAnalysis() throws Exception {
        File testFile = new File("src/test/java/com/damianpeaf/test.olc");
        OLCLexer lexer = new OLCLexer(new FileReader(testFile));
        OLCParser parser = new OLCParser(lexer);

        try {
            parser.parse();

        }catch (Exception e){
            System.out.println(e.getMessage());

        }
        //parser.errorTable.printErrors();

    }

}
