package OLCCompiler.Set;

public class OLCComprehensionSet extends OLCSet{

    public OLCComprehensionSet() {
        super();
        this.type = OLCSetType.COMPREHENSION;
    }

    @Override
    public void printSet() {
        System.out.println("Comprehension Set: " + this.name);
        System.out.println("Elements: " + this.elements.get(0).token + " ~ " + this.elements.get(1).token);
    }

}
