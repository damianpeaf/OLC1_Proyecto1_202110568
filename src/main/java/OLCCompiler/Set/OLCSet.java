package OLCCompiler.Set;

import java.util.ArrayList;

public abstract class OLCSet {

    public String name;
    protected String type;
    public ArrayList<OLCSetElement> elements = new ArrayList<>();

    public OLCSet() {}

    public void setName(String name) {
        this.name = name;
    }

    public void addElements(ArrayList<OLCSetElement> elements){
        elements.forEach(element -> this.elements.add(element));
    }


    public abstract void printSet();
}

