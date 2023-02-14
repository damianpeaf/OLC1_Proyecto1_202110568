package OLCCompiler.DFA;

import java.util.HashSet;
import java.util.Set;

public class Next {

    public Set<String> token;
    public Integer node;
    public Set<Integer> next;

    public Next(Set<String> token, Integer node, Set<Integer> next){
        this.token = token;
        this.node = node;
        this.next = next;
    }


    public void addNext(Set<Integer> next){
        this.next.addAll(next);
    }
}
