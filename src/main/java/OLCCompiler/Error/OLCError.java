package OLCCompiler.Error;

import OLCCompiler.OLCParserSym;
import java_cup.runtime.Symbol;

import java.util.ArrayList;
import java.util.List;

public class OLCError {

    private String type;
    private Symbol token;
    private ArrayList<String> expected = null;
    private String message = null;

    public OLCError(String type, Object token) {

        if ((type == ErrorType.LEXIC || type == ErrorType.SYNTAX) && token instanceof Symbol) {
            this.type = type;
            this.token = (Symbol) token;
        }else if(type == ErrorType.RUNTIME && token instanceof String){
            this.message = (String) token;
            this.type = type;
        } else {
            throw new IllegalArgumentException("Tipo de error no valido");
        }
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
        }else if(this.type == ErrorType.SYNTAX){
            String expected = this.expected != null ? "Se esperaba: " + this.expected : "";
            return "No se esperaba el token: " + token.value + " ("+ OLCParserSym.terminalNames[this.token.sym]+"). " + expected;
        }else{
            return this.message;
        }
    }

    public String getType() {
        String type = "";

        switch (this.type) {
            case ErrorType.LEXIC:
                type = "Lexico";
                break;
            case ErrorType.SYNTAX:
                type = "Sintactico";
                break;
            case ErrorType.RUNTIME:
                type = "Ejecucion";
                break;
        }
        return type;
    }

    public int getLine(){
        if (this.token != null) {
            return this.token.left;
        }
        return -1;
    }

    public int getColumn(){
        if (this.token != null) {
            return this.token.right;
        }
        return -1;
    }

}

