package OLCCompiler.DFA;

import java.util.Set;

public class AbbreviateTransition {

    public State prevState;
    public State nextState;
    public Set<Object> tokens;

    public AbbreviateTransition (State prevState, State nextState, Object token){
        this.prevState = prevState;
        this.nextState = nextState;
        this.tokens = new java.util.HashSet<>();
        this.tokens.add(token);
    }

    public void addToken(Object token){
        this.tokens.add(token);
    }

    public String getTokensString(){
        String s = "";
        Object [] tokensArray = this.tokens.toArray();

        for(int i = 0; i < this.tokens.size(); i++){

            String token = tokensArray[i].toString();

            if(token.equals(" ")){
                token = "\\\" \\\"";
            }else if(token.equals("\n")){
                token = "\\\\n";
            }

            s += token;
            if (i < this.tokens.size() - 1) s += ", ";
        }
        return s;
    }
}
