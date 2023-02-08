package com.damianpeaf;

import java_cup.runtime.Symbol;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testAnalysis() throws Exception {
        File testFile = new File("src/test/java/com/damianpeaf/test.olc");
        OLCLexer lexer = new OLCLexer(new FileReader(testFile));
        OLCParser parser = new OLCParser(lexer);

        /* open input files, etc. here */
        Symbol parse_tree = null;

        parse_tree = parser.debug_parse();
        System.out.println("Parse tree: " + parse_tree);

    }

}
