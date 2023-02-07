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
    return symbol(ParserSym.EOF);
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
    <YYINITIAL> "CONJ" { return symbol(ParserSym.SET_DECLARATION); }

    <YYINITIAL> {
        /* -- General tokens -- */

        /* COMMENTS */
        {Comment} { /* ignore */ }

        /* IDENTIFIERS */
        {Identifier} { return symbol(ParserSym.IDENTIFIER, yytext()); }

        /* BRACES */
        "{" { return symbol(ParserSym.LBRACE); }
        "}" { return symbol(ParserSym.RBRACE); }

        /* COLON */
        ":" { return symbol(ParserSym.COLON); }

        /* SEMICOLON */
        ";" { return symbol(ParserSym.SEMICOLON); }

        /* ARROW */
        "->" { return symbol(ParserSym.ARROW); }

        /* SCOPE BREAK */
        "%%" { return symbol(ParserSym.SCOPE_BREAK); }

        /* WHITESPACE */
        {WhiteSpace} { /* ignore */ }

        /* STRING START */
        \" { string.setLength(0); yybegin(STRING); }

        /* -- SET NOTATION -- */

        /* TILDE */
        "~" { return symbol(ParserSym.TILDE); }

        /* COMMA */
        "," { return symbol(ParserSym.COMMA); }

        /* DIGIT */
        {Digit} { return symbol(ParserSym.DIGIT, Integer.valueOf(yytext())); }

        /* LOWERCASE */
        {Lowercase} { return symbol(ParserSym.LOWERCASE, yytext()); }

        /* UPPERCASE */
        {Uppercase} { return symbol(ParserSym.UPPERCASE, yytext()); }

        /* ASCII */
        {Ascii} { return symbol(ParserSym.ASCII, yytext()); }

        /* -- REGEX NOTATION -- */

        /* AND */
        "." { return symbol(ParserSym.AND); }

        /* OR */
        "|" { return symbol(ParserSym.OR); }

        /* KLEENE */
        "*" { return symbol(ParserSym.KLEENE); }

        /* PLUS */
        "+" { return symbol(ParserSym.PLUS); }

        /* QUESTION */
        "?" { return symbol(ParserSym.QUESTION); }

        /* WORD */
        {Word} { return symbol(ParserSym.WORD, yytext()); }

        /* -- ESCAPED CHARACTERS -- */

        /* LINEBREAK */
        "\\n" { return symbol(ParserSym.ESCAPED_LINEBREAK); }

        /* SINGLE QUOTE */
        "\\'" { return symbol(ParserSym.ESCAPED_SINGLE_QUOTE); }

        /* DOUBLE QUOTE */
        "\\\"" { return symbol(ParserSym.ESCAPED_DOUBLE_QUOTE); }
    }

    <STRING> {
        // String end
        \" { yybegin(YYINITIAL);
             return symbol(sym.STRING_LITERAL, string.toString());
           }

        [^\n\r\"\\]+                   { string.append( yytext() ); }
        \\t                            { string.append('\t'); }
        \\n                            { string.append('\n'); }

        \\r                            { string.append('\r'); }
        \\\"                           { string.append('\"'); }
        \\                             { string.append('\\'); }
    }

    /* error fallback */
    [^]                              { return symbol(ParserSym.ERROR, yytext()); }