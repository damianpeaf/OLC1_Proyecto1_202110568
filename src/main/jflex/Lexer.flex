package com.damianpeaf;
import java_cup.runtime.*;

%%

%class OLCLexer
%unicode
%cup
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}
%eofval{
    return symbol(OLCParserSym.EOF);
%eofval}

    LineTerminator = \r|\n|\r\n
    InputCharacter = [^\r\n]
    WhiteSpace     = {LineTerminator} | [ \t\f]

    /* COMMENTS */

    Comment = {MultiLineComment} | {SingleLineComment}

    MultiLineComment = "<!" [^*] ~"!>" | "<!" "!"+ ">"
    SingleLineComment = "//" {InputCharacter}* {LineTerminator}?

    Identifier = [:jletter:] [:jletterdigit:]*
    Digit = [0-9]
    Lowercase = [a-z]
    Uppercase = [A-Z]
    Ascii = [\x20-\x2F\x3A-\x40\x5B-\x60\x7B-\x7E]
    Word = \w+


%state STRING
%%

    /* KEYWORDS */
    <YYINITIAL> "CONJ" { return symbol(OLCParserSym.SET_DECLARATION); }

    <YYINITIAL> {
        /* -- General tokens -- */

        /* COMMENTS */
        {Comment} { /* ignore */ }

        /* IDENTIFIERS */
        {Identifier} { return symbol(OLCParserSym.IDENTIFIER, yytext()); }

        /* BRACES */
        "{" { return symbol(OLCParserSym.LBRACE); }
        "}" { return symbol(OLCParserSym.RBRACE); }

        /* COLON */
        ":" { return symbol(OLCParserSym.COLON); }

        /* SEMICOLON */
        ";" { return symbol(OLCParserSym.SEMICOLON); }

        /* ARROW */
        "->" { return symbol(OLCParserSym.ARROW); }

        /* SCOPE BREAK */
        "%%" { return symbol(OLCParserSym.SCOPE_BREAK); }

        /* WHITESPACE */
        {WhiteSpace} { /* ignore */ }

        /* STRING START */
        \" { string.setLength(0); yybegin(STRING); }

        /* -- SET NOTATION -- */

        /* TILDE */
        "~" { return symbol(OLCParserSym.TILDE); }

        /* COMMA */
        "," { return symbol(OLCParserSym.COMMA); }

        /* DIGIT */
        {Digit} { return symbol(OLCParserSym.DIGIT, Integer.valueOf(yytext())); }

        /* LOWERCASE */
        {Lowercase} { return symbol(OLCParserSym.LOWERCASE, yytext()); }

        /* UPPERCASE */
        {Uppercase} { return symbol(OLCParserSym.UPPERCASE, yytext()); }

        /* ASCII */
        {Ascii} { return symbol(OLCParserSym.ASCII, yytext()); }

        /* -- REGEX NOTATION -- */

        /* AND */
        "." { return symbol(OLCParserSym.AND); }

        /* OR */
        "|" { return symbol(OLCParserSym.OR); }

        /* KLEENE */
        "*" { return symbol(OLCParserSym.KLEENE); }

        /* PLUS */
        "+" { return symbol(OLCParserSym.PLUS); }

        /* QUESTION */
        "?" { return symbol(OLCParserSym.QUESTION); }

        /* WORD */
        {Word} { return symbol(OLCParserSym.WORD, yytext()); }

        /* -- ESCAPED CHARACTERS -- */

        /* LINEBREAK */
        "\\n" { return symbol(OLCParserSym.ESCAPED_LINEBREAK); }

        /* SINGLE QUOTE */
        "\\'" { return symbol(OLCParserSym.ESCAPED_SINGLE_QUOTE); }

        /* DOUBLE QUOTE */
        "\\\"" { return symbol(OLCParserSym.ESCAPED_DOUBLE_QUOTE); }
    }

    <STRING> {
        // String end
        \" { yybegin(YYINITIAL);
             return symbol(OLCParserSym.STRING_LITERAL, string.toString());
           }

        [^\n\r\"\\]+                   { string.append( yytext() ); }
        \\t                            { string.append('\t'); }
        \\n                            { string.append('\n'); }

        \\r                            { string.append('\r'); }
        \\\"                           { string.append('\"'); }
        \\                             { string.append('\\'); }
    }

    /* error fallback */
    [^]                              { return symbol(OLCParserSym.ERROR, yytext()); }