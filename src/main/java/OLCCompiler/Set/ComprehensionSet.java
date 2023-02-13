package OLCCompiler.Set;

public class ComprehensionSet extends Set {

    public ComprehensionSet() {
        super();
        this.type = SetType.COMPREHENSION;
    }

    @Override
    public void printSet() {
        System.out.println("Comprehension Set: " + this.name);
        System.out.println("Elements: " + this.elements.get(0).token + " ~ " + this.elements.get(1).token);
    }

}
