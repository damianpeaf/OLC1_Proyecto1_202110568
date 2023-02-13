package OLCCompiler.Set;

public class SetReference {

    public String name;
    public Set set;
    public SetsTable table;

    public SetReference(String name, SetsTable table){
        this.name = name;
        this.table = table;
    }

    public Set getSet(){
        if (set == null){
            // set = table.getSet(name);
        }
        return set;
    }

}
