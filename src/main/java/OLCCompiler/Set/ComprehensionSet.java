package OLCCompiler.Set;

import OLCCompiler.Error.ErrorTable;
import OLCCompiler.Error.ErrorType;
import OLCCompiler.Error.OLCError;

import java.util.HashSet;

public class ComprehensionSet extends Set {

    private String rangeType = null;

    public ComprehensionSet() {
        super();
        this.type = SetType.COMPREHENSION;
    }

    @Override
    public void printSet() {
        System.out.println("Comprehension Set: " + this.name);
        System.out.println("Definition: " + this.elements.get(0).token + " ~ " + this.elements.get(1).token);
        System.out.println("Elements: " + this.getElements());
    }

    public boolean validate(ErrorTable errorTable){

        // Size validation
        if(this.elements.size() != 2){
            errorTable.add(new OLCError(ErrorType.RUNTIME, "El conjunto '" + this.name + "' necesita dos elementos para ser declarado"));
            return false;
        }

        // Range type validation

        if(this.elements.get(0).type.equals(this.elements.get(1).type)) {
            this.rangeType = this.elements.get(0).type;
        }else{
            errorTable.add(new OLCError(ErrorType.RUNTIME, "El conjunto '" + this.name + "' no puede ser declarado con elementos de diferentes tipos. Se recibió: " + this.elements.get(0).type + " y " + this.elements.get(1).type));
            return false;
        }

        return true;
    }

    @Override
    public java.util.Set<String> getElements() {
        if(this.rangeType == null){
            return null;
        }

        java.util.Set<String> elements = new HashSet<>();
        int start = this.elements.get(0).token.charAt(0);
        int end = this.elements.get(1).token.charAt(0);

        switch (this.rangeType) {
            case SetElementType.ASCII:
                for (int i = start; i <= end; i++) {

                    if ((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                        continue;
                    }

                    elements.add(Character.toString((char) i));
                }
                return elements;

            default:
                for (int i = start; i <= end; i++) {
                    elements.add(Character.toString((char) i));
                }
                return elements;

        }
    }
}
