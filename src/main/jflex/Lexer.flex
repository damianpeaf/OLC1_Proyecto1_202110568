package OLCCompiler;
import java_cup.runtime.*;

%%

%class OLCLexer
%public
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
%state SET
%state SET_ELEMENT
%state SET_OPERATOR
%state REGEX
%state REGEX_TEST
%state REGEX_EXPRESSION
%state REGEX_STRING
%state SET_REFERENCE

%%

    /* COMMENTS */
    {Comment} { /* ignore */ }

    /* WHITESPACE */
    {WhiteSpace} { /* ignore */ }

    <YYINITIAL> {
        /* -- root tokens -- */

        /* BRACES */
        "{" { return symbol(OLCParserSym.LBRACE); }
        "}" { return symbol(OLCParserSym.RBRACE); }

        /* KEYWORDS */
        "CONJ" { yybegin(SET); return symbol(OLCParserSym.SET_DECLARATION); }

        /* SCOPE BREAK */
        "%%" { return symbol(OLCParserSym.SCOPE_BREAK); }

        /* SEMICOLON */
        ";" { yybegin(YYINITIAL); return symbol(OLCParserSym.SEMICOLON); }

        /* IDENTIFIERS */
        {Identifier} { yybegin(REGEX); return symbol(OLCParserSym.IDENTIFIER, yytext()); }

    }

    <SET> {

        /* COLON */
        ":" { return symbol(OLCParserSym.COLON); }

        /* IDENTIFIERS */
        {Identifier} { return symbol(OLCParserSym.IDENTIFIER, yytext()); }

        /* ARROW */
        "->" { yybegin(SET_ELEMENT);  return symbol(OLCParserSym.ARROW); }

    }

    <SET_ELEMENT> {

        /* ASCII */
        {Ascii} { yybegin(SET_OPERATOR); return symbol(OLCParserSym.ASCII, yytext()); }

        /* DIGIT */
        {Digit} { yybegin(SET_OPERATOR); return symbol(OLCParserSym.DIGIT, Integer.valueOf(yytext())); }

        /* LOWERCASE */
        {Lowercase} { yybegin(SET_OPERATOR); return symbol(OLCParserSym.LOWERCASE, yytext()); }

        /* UPPERCASE */
        {Uppercase} { yybegin(SET_OPERATOR); return symbol(OLCParserSym.UPPERCASE, yytext()); }

    }


    <SET_OPERATOR> {

        /* COMMA */
        "," { yybegin(SET_ELEMENT); return symbol(OLCParserSym.COMMA); }

        /* TILDE */
        "~" { yybegin(SET_ELEMENT); return symbol(OLCParserSym.TILDE); }

        /* SEMICOLON */
        ";" { yybegin(YYINITIAL); return symbol(OLCParserSym.SEMICOLON); }
    }

    <REGEX> {
        /* COLON */
        ":" { yybegin(REGEX_TEST); return symbol(OLCParserSym.COLON); }

        /* ARROW */
        "->" { yybegin(REGEX_EXPRESSION);  return symbol(OLCParserSym.ARROW); }
    }


    <REGEX_TEST> {

        /* STRING START */
        \" { string.setLength(0); yybegin(STRING); }

    }

    <REGEX_EXPRESSION> {

        /* -- OPERATORS -- */

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

        /* -- ESCAPED CHARACTERS -- */

        /* LINEBREAK */
        "\\n" { return symbol(OLCParserSym.ESCAPED_LINEBREAK); }

        /* SINGLE QUOTE */
        "\\'" { return symbol(OLCParserSym.ESCAPED_SINGLE_QUOTE); }

        /* DOUBLE QUOTE */
        "\\\"" { return symbol(OLCParserSym.ESCAPED_DOUBLE_QUOTE); }

        /* SEMICOLON */
        ";" { yybegin(YYINITIAL); return symbol(OLCParserSym.SEMICOLON); }

        /* -- OTHER REGEX TERMINALS -- */

        /* STRING START */
        \" { string.setLength(0); yybegin(REGEX_STRING); }

        /* BRACES */
        "{" { yybegin(SET_REFERENCE);  return symbol(OLCParserSym.LBRACE); }

        /* DIGIT */
        {Digit} { yybegin(SET_OPERATOR); return symbol(OLCParserSym.DIGIT, Integer.valueOf(yytext())); }

        /* WORD */
        {Word} { return symbol(OLCParserSym.WORD, yytext()); }
    }

    <SET_REFERENCE> {
        /* RBRACE */
        "}" { yybegin(REGEX_EXPRESSION); return symbol(OLCParserSym.RBRACE); }

        /* IDENTIFIERS */
        {Identifier} { return symbol(OLCParserSym.IDENTIFIER, yytext()); }
    }

    <REGEX_STRING> {
        // String end
        \" { yybegin(REGEX_EXPRESSION);
             return symbol(OLCParserSym.STRING_LITERAL, string.toString());
           }

        [^\n\r\"\\]+                   { string.append( yytext() ); }
        \\t                            { string.append('\t'); }
        \\n                            { string.append('\n'); }

        \\r                            { string.append('\r'); }
        \\\"                           { string.append('\"'); }
        \\                             { string.append('\\'); }
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

    /* -- NO SCOPED TOKENS -- */

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

    /* error fallback */
    [^]                              { return symbol(OLCParserSym.LEXICAL_ERROR, yytext()); }