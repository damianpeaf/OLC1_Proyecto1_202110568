package OLCCompiler;

import java_cup.runtime.Symbol;

public class OLCError {

    public String type;
    public String message;
    public Symbol token;

    public OLCError(String type, String message, Symbol token) {

        this.type = type;
        this.message = message;
        this.token = token;

    }

}

