public class FU {
    String type; // floating point or int
    String name; // ex. load1 load2 add1
    String op;
    String val;
    boolean busy;
    int latency; // number of clock cycles it takes to execute
    String vj;  // source register if ready
    String vk;  // other source register if exists or ready
    String qj; // rob entry for source register if not ready
    String qk; // rob entry for other source register if not ready
    int dest; // rob entry
    String a; // offset for load and store instructions
    Instruction i;
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
	Entry entry=Scoreboard.rob[dest]; // el mkan elly hakhzn feeh el result
	 val = null;
    if (op.equalsIgnoreCase("load")) {
        // call load method
    	
    	
    }
   else if (op.equalsIgnoreCase("store")) {
        // call store method
    }
   else if (op.equalsIgnoreCase("add")) {
        // add method
	   if(qj==null&&qk==null){//they are in the registers
		   int result=Scoreboard.p.registers.get(vj)+Scoreboard.p.registers.get(vk);
		   val=String.valueOf(result);
		   
	   }
	   else{//go and take it from the ROB
		   int result;
		   if(qj!=null ){//i have to get vj from the ROB
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qj)];
			   
			   result=Scoreboard.p.registers.get(vk)+Integer.parseInt(entry1.value);
			   
			   
		   }
		   else {//qk is not null
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qk)];
			  result =Scoreboard.p.registers.get(vj)+ Integer.parseInt(entry1.value);
		   }
		   
		 
		   val=String.valueOf(result);
	   }
	   
    }
   else if (op.equalsIgnoreCase("sub")||op.equalsIgnoreCase("beq")) {
	   if(qj==null&&qk==null){//they are in the registers
		   int result=Scoreboard.p.registers.get(vj)-Scoreboard.p.registers.get(vk);
		   val=String.valueOf(result);
		   
	   }
	   else{//go and take it from the ROB
		   int result;
		   if(qj!=null ){//i have to get vj from the ROB
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qj)];
			   
			   result=Integer.parseInt(entry1.value)-Scoreboard.p.registers.get(vk);
			   
			   
		   }
		   else {//qk is not null
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qk)];
			  result =Scoreboard.p.registers.get(vj)- Integer.parseInt(entry1.value);
		   }
		   
		 
		   val=String.valueOf(result);
	   }

    }
   else if (op.equalsIgnoreCase("mul")) {
	   if(qj==null&&qk==null){//they are in the registers
		   int result=Scoreboard.p.registers.get(vj)*Scoreboard.p.registers.get(vk);
		   val=String.valueOf(result);
		   
	   }
	   else{//go and take it from the ROB
		   int result;
		   if(qj!=null ){//i have to get vj from the ROB
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qj)];
			   
			   result=Integer.parseInt(entry1.value)*Scoreboard.p.registers.get(vk);
			   
			   
		   }
		   else {//qk is not null
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qk)];
			  result =Scoreboard.p.registers.get(vj)* Integer.parseInt(entry1.value);
		   }
		   
		 
		   val=String.valueOf(result);
	   }
	   

    }
   else if (op.equalsIgnoreCase("addi")) {
	   if(qj==null){//they are in the registers
		   int result=Scoreboard.p.registers.get(vj)+Integer.parseInt(a);
		   val=String.valueOf(result);
		   
	   }
	   else{
		   Entry entry1=Scoreboard.rob[Integer.parseInt(qj)];
		   int result=Integer.parseInt(entry1.value)+Integer.parseInt(a);
		   val=String.valueOf(result);
		   
	   }
	   

    }
   else if(op.equalsIgnoreCase("nand")){
	   if(qj==null&&qk==null){//they are in the registers
		   int result=~(Scoreboard.p.registers.get(vj)&Scoreboard.p.registers.get(vk));
		   val=String.valueOf(result);
		   
	   }
	   else{//go and take it from the ROB
		   int result;
		   if(qj!=null ){//i have to get vj from the ROB
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qj)];
			   
			   result=~(Integer.parseInt(entry1.value)&Scoreboard.p.registers.get(vk));
			   
			   
		   }
		   else {//qk is not null
			   Entry entry1=Scoreboard.rob[Integer.parseInt(qk)];
			  result =~(Scoreboard.p.registers.get(vj)& Integer.parseInt(entry1.value));
		   }
		   
		 
		   val=String.valueOf(result);
	   
   }
   }
    //entry.value=val;
    // add other instructions
}
////////////////////weam
public void wb(){
String value= val;
//String value="15"; //for testing purpose;
if(value!=null){
Entry entry=Scoreboard.rob[dest]; // el mkan elly hakhzn feeh el result
entry.value=value;
//System.out.println(entry.dest);
Scoreboard.p.registers.put(entry.dest, Integer.parseInt(value));
}
}

//this method to clear the FU
public void clearFU(){
op = null;
busy=false;
vj=null;  // source register if ready
vk=null;  // other source register if exists or ready
qj=null; // rob entry for source register if not ready
qk=null; // rob entry for other source register if not ready
dest= 0; // rob entry 
a=null; // offset for load and store instructions

}
/////////////end weam


}