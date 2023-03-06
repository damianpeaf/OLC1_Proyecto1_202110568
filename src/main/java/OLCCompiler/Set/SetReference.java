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
        return table.getSet(name);
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
