package OLCCompiler.Tree;
import java.util.*;

public class Node {

    public Node left = null;
    public Node right = null;
    public int number;
    public String type;
    public Object value;

    public boolean nullable;
    public Set<Integer> firstPos = new HashSet();
    public Set<Integer> lastPos = new HashSet();



    public Node(int number, String type, Object value) {
        this.number = number;
        this.value = value;
        this.type = type;
    }

    public Node(int number, String type, Object value, Node left, Node right) {
        this.number = number;
        this.value = value;
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Node(int number, String type, Object value, Node left) {
        this.number = number;
        this.value = value;
        this.left = left;
        this.type = type;
    }

    public String getGraphvizNode() {
        switch (this.type){
            case NodeType.NODE_I:
                return "I_" + this.number;
            case NodeType.NODE_ACCEPT:
                return "I_ACEPTANCE_" + this.number;
            case NodeType.NODE_PLUS:
                return "PLUS_" + this.number;
            case NodeType.NODE_OR:
                return "OR_" + this.number;
            case NodeType.NODE_KLEENE:
                return "KLEENE_" + this.number;
            case NodeType.NODE_OPTIONAL:
                return "OPTIONAL_" + this.number;
            case NodeType.NODE_AND:
                return "AND_" + this.number;
            default:
                return "NODE_" + this.number;
        }
    }


    public String getGraphvizLabel() {
        String value = this.value.toString();

        if(value.equals(" ")){
            value = "\\\" \\\"";
        }else if(value.equals("\n")){
            value = "\\\\n";
        }
        if (this.type.equals(NodeType.NODE_PLUS) || this.type.equals(NodeType.NODE_KLEENE) || this.type.equals(NodeType.NODE_OPTIONAL) || this.type.equals(NodeType.NODE_OR) || this.type.equals(NodeType.NODE_AND)) {
            return "<<table border=\"0\">\n" +
                    "            <tr>\n" +
                    "                <td colspan=\"4\" align=\"center\">"+(this.nullable ? "V" : "F")+"</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td align=\"left\">"+this.firstPos+"</td>\n" +
                    "                \n" +
                    "                <td align=\"center\" colspan=\"2\"><b>"+this.value.toString()+"</b></td>\n" +
                    "                <td align=\"right\">"+this.firstPos+"</td>\n" +
                    "            </tr>\n" +
                    "        </table>>";
        }else{
            return "<<table border=\"0\">\n" +
                    "            <tr>\n" +
                    "                <td colspan=\"4\" align=\"center\">"+(this.nullable ? "V" : "F")+"</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td align=\"left\">"+this.firstPos+"</td>\n" +
                    "                \n" +
                    "                <td align=\"center\" colspan=\"2\"><b>"+this.value.toString()+"</b></td>\n" +
                    "                <td align=\"right\">"+this.firstPos+"</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td colspan=\"4\" align=\"center\">"+this.number+"</td>\n" +
                    "            </tr>\n" +
                    "        </table>>";
        }
    }


}
