package OLCCompiler.Set;

import java.util.ArrayList;

public class SetsTable {

    public ArrayList<Set> sets;

    public SetsTable() {
        this.sets = new ArrayList<Set>();
    }

    public void add (Set set) {
        sets.add(set);
    }

    public void print(){
        System.out.println("Sets Table");
        System.out.println("----------");
        sets.forEach(Set::printSet);
    }
}
