package OLCCompiler.DFA;

import java.util.HashSet;
import java.util.Set;

public class Next {

    public Set<Object> token;
    public Integer node;
    public Set<Integer> next;

    public Next(Set<Object> token, Integer node, Set<Integer> next){
        this.token = token;
        this.node = node;
        this.next = next;
    }


    public void addNext(Set<Integer> n){
        this.next.addAll(n);
    }
}
