/**
 * Created by amallasheen on 11/27/15.
 */
public class Instruction {
    String name;
    String rs;
    String rt;
    String rd;
    String type;
    String lastCycle;
    public Instruction(String n, String source1, String source2, String dest,String t) {
    	
    	// checks if source1 contsins a number then use amal's default else use omar's instruction call
    	if (source1.matches(".*[0-9].*")) 
    	{
            name = n;
            rs = source1;
            rt = source2;
            rd = dest;
            type = t;
		}
    	else {
			name = n;
			type = source1;
			rd = source2;
			rs = dest;
			rd = t;
		}
    }
    public String toString() {
        return "[Name: " + name + ", rs: " + rs + ", rt: " + rt + ", rd: " + rd + "Type: " + type + "]";
    }
    public boolean equals(Instruction i) {
        return (i.name.equals(name) && i.rs.equals(rs) && i.rt.equals(rt) && i.rd.equals(rd) && i.type.equals(type));
    }

}
