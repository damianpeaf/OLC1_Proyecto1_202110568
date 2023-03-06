package OLCCompiler.Set;

import OLCCompiler.Error.ErrorTable;

import java.util.HashSet;

public class ExtensionSet extends Set {

    public ExtensionSet() {
        super();
        this.type = SetType.EXTENSION;
    }

    @Override
    public void printSet() {
        System.out.println("Extension Set: " + this.name);
        StringBuilder elements = new StringBuilder();
        this.elements.forEach(element -> elements.append(element.token).append(", "));
        System.out.println("Definition: " + elements);
        System.out.println("Elements: " + this.getElements());
    }

    @Override
    public boolean validate(ErrorTable errorTable) {
        return true;
    }

    @Override
    public java.util.Set<String> getElements() {
        java.util.Set<String> elements = new HashSet<>();
        for(SetElement e : this.elements){
            elements.add(e.token);
        }
        return elements;
    }
}
