package OLCCompiler.Error;

import java.util.ArrayList;

public class ErrorTable {

    public ArrayList<OLCError> errors;

    public ErrorTable() {
        errors = new ArrayList<OLCError>();
    }

    public void add (OLCError error) {
        errors.add(error);
    }

    public void printErrors () {
        for (OLCError error : errors) {
            System.out.println(error.getMessage() + " at line " + error.getLine() + " and column " + error.getColumn());
        }
    }

}
