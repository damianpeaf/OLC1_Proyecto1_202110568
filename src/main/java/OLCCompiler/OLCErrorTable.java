package OLCCompiler;

import java.util.ArrayList;

public class OLCErrorTable {

    public ArrayList<OLCError> errors;

    public OLCErrorTable () {
        errors = new ArrayList<OLCError>();
    }

    public void add (OLCError error) {
        errors.add(error);
    }

    public void printErrors () {
        for (OLCError error : errors) {
            System.out.println(error.getMessage());
        }
    }

}
