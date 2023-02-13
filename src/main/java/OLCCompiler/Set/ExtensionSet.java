package OLCCompiler.Set;

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
        System.out.println("Elements: " + elements.toString());
    }

}
