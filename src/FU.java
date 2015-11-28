/**
 * Created by amallasheen on 11/27/15.
 */
public class FU {
    String type; // floating point or int
    String name; // ex. load1 load2 add1
    String op;
    boolean busy;
    int latency; // number of clock cycles it takes to execute
    String vj;  // source register if ready
    String vk;  // other source register if exists or ready
    String qj; // rob entry for source register if not ready
    String qk; // rob entry for other source register if not ready
    int dest; // rob entry
    String a; // offset for load and store instructions
public FU(String t,String n,int l) {
    type = t;
    name = n;
    latency = l;
    busy = false;
}
public String toString() {
    return "[Type " + type + ", Name: " + name + ", OP: " + op + ", Busy: " + busy + ", Latency: " + latency + ", Vj: " + vj + ", Vk: " + vk + ", Qj: " + qj + ", Qk: " + qk + ", Dest: " + dest + ", A: " + a + "]";
}
public void execute() {
    if (op.equalsIgnoreCase("load")) {
        // call load method
    }
   else if (op.equalsIgnoreCase("store")) {
        // call store method
    }
   else if (op.equalsIgnoreCase("add")) {
        // add method
    }
   else if (op.equalsIgnoreCase("sub")) {

    }
    // add other instructions
}


}
