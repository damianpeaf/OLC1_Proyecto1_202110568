package OLCCompiler.Set;

public class SetReference {

    public String name;
    private Set set;
    public SetsTable table;

    public SetReference(String name, SetsTable table){
        this.name = name;
        this.table = table;
    }

    public Set getSet(){
        //TODO: get set from table
        if (set == null){
            // set = table.getSet(name);
        }

        return set;
    }

    @Override
    public String toString() {
        return "{"+this.name +"}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SetReference){
            return this.name.equals(((SetReference) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
