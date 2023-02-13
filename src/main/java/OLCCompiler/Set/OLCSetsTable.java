package OLCCompiler.Set;

import OLCCompiler.OLCError;

import java.util.ArrayList;

public class OLCSetsTable {

    public ArrayList<OLCSet> sets;

    public OLCSetsTable() {
        this.sets = new ArrayList<OLCSet>();
    }

    public void add (OLCSet set) {
        sets.add(set);
    }

    public void print(){
        System.out.println("Sets Table");
        System.out.println("----------");
        sets.forEach(OLCSet::printSet);
    }
}
