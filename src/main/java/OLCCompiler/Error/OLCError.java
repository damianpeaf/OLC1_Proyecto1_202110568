package OLCCompiler.Error;

import OLCCompiler.OLCParserSym;
import java_cup.runtime.Symbol;

import java.util.ArrayList;
import java.util.List;

public class OLCError {

    private String type;
    private Symbol token;
    private ArrayList<String> expected = null;

    public OLCError(String type, Symbol token) {

        if (type == ErrorType.LEXIC || type == ErrorType.SYNTAX) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Tipo de error no valido");
        }

        this.token = token;
    }

    public OLCError(String type, Symbol token, ArrayList<String> expected) {
        if (type == ErrorType.LEXIC || type == ErrorType.SYNTAX) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Tipo de error no valido");
        }

        this.token = token;
        this.expected = expected;
    }

    public String getMessage(){
        if(this.type == ErrorType.LEXIC){
            return "El caracter " + this.token.value + " no pertenece al lenguaje";
        }else{
            System.out.println(this.token);
            System.out.println(this.token.value);
            System.out.println(this.token.sym);
            System.out.println();

            String expected = this.expected != null ? "Se esperaba: " + this.expected : "";

            return "No se esperaba el token: " + token.value + " ("+ OLCParserSym.terminalNames[this.token.sym]+"). " + expected;
        }
    }

    public String getType() {
        return type == ErrorType.LEXIC ? "Lexico" : "Sintactico";
    }

    public int getLine(){
        return this.token.left;
    }

    public int getColumn(){
        return this.token.right;
    }

}

