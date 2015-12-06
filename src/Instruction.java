public class Instruction {
    String name;
    String rs;
    String rt;
    String rd;
    String type;
    String offset;//modified
    int  PC;
    int lastCycle;
    public Instruction(String n, String source1, String source2, String dest,String t,String o) {
        name = n;
        rs = source1;
        rt = source2;
        rd = dest;
        type = t;
        offset=o;
    }
    public void set_pc(int p){
    	PC=p;
    }
    public int get_pc(){
    	return PC;
    }
    public String toString() {
        return "[Name: " + name + ", rs: " + rs + ", rt: " + rt + ", rd: " + rd + "Type: " + type + "]";
    }
    public boolean equals(Instruction i) {
        return (i.name.equals(name) && i.rs.equals(rs) && i.rt.equals(rt) && i.rd.equals(rd) && i.type.equals(type));
    }

}