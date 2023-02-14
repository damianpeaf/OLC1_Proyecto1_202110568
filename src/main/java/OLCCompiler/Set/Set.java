package OLCCompiler.Set;

import java.util.ArrayList;

public abstract class Set {

    public String name;
    protected String type;
    public ArrayList<SetElement> elements = new ArrayList<>();

    public Set() {}

    public void setName(String name) {
        this.name = name;
    }

    public void addElements(ArrayList<SetElement> elements){
        elements.forEach(element -> this.elements.add(element));
    }

    public abstract void printSet();

    public abstract java.util.Set<String> getElements();
}

