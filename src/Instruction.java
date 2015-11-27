/**
 * Created by amallasheen on 11/27/15.
 */
public class Instruction {
    String name;
    String rs;
    String rt;
    String rd;
    String type;
    public Instruction(String n, String source1, String source2, String dest,String t) {
        name = n;
        rs = source1;
        rt = source2;
        rd = dest;
        type = t;
    }
    public String toString() {
        return "[Name: " + name + ", rs: " + rs + ", rt: " + rt + ", rd: " + rd + "Type: " + type + "]";
    }

}
