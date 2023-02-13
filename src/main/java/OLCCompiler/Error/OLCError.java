package OLCCompiler.Error;

import java_cup.runtime.Symbol;

public class OLCError {

    private String type;
    private Symbol token;

    public OLCError(String type, Symbol token) {

        if (type == ErrorType.LEXIC || type == ErrorType.SYNTAX) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Tipo de error no valido");
        }

        this.token = token;
    }

    public String getMessage(){
        if(this.type == ErrorType.LEXIC){
            return "El caracter " + this.token.value + " no pertenece al lenguaje";
        }else{
            return "No se esperaba el token: " + this.token.value;
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

