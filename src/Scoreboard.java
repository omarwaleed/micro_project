import java.util.*;

public class Scoreboard {
   static Scoreboard sb;
   Hashtable<String,Entry> registerStatus;
   Entry[] rob;  // reorder buffer is an array of entries 
   int head;  // rob head pointer
   int tail;// rob tail pointer
   int robSize;
	ArrayList<FU>functionalUnits; // keep all the reservation stations
    Hashtable<Instruction,String>instructions; // each instruction is mapped to its current phase

   private Scoreboard(){
	   registerStatus = new Hashtable<String,Entry>();
	   head = tail = 0;
       functionalUnits = new ArrayList<FU>();
   }
   public static Scoreboard getInstance() {  // use this method to get access the score board
	   if (sb == null) 
		   sb = new Scoreboard();
	   return sb;
   }
  public int increment(int i) {  // use this method to increment either the head or the tail and pass to it the int you want to increment
	  if (i == 9) 
		  return 0;
	      return ++i;
  }
  public boolean robFull() {  // tested
	 for(int i = 0; i < rob.length;i++) {
		 if (!rob[i].occupied)
			 return false;
	 }
	   return true;
  }
  public void insertFU(String type, String name, int latency) { //tested
      FU fu = new FU(type,name,latency);
      functionalUnits.add(fu);
  }
  public boolean fullFuncUnit(String type) { // tested
      for(int i = 0; i<functionalUnits.size();i++) {
          FU fu = functionalUnits.get(i);
          if (!fu.busy && fu.type.equalsIgnoreCase(type))
              return false;
      }
      return true;
  }
    public void insertROB(Entry entry) { //tested
        rob[tail] = entry;
       tail = increment(tail);
    }
    public void removeFromROB() { // tested
        rob[head] = null;
       head = increment(head);
    }
    public static void main(String[]args) {
        Scoreboard sb = Scoreboard.getInstance();
        Entry e1 = new Entry("LD","F6","",false);
       // System.out.println(e1);
        sb.insertROB(e1);
        sb.insertROB(e1);
        System.out.println("head: "+ sb.head + " tail " + sb.tail);
        sb.removeFromROB();
        System.out.println("after removing: head: "+ sb.head + " tail " + sb.tail);
//        sb.insertFU("FP","addd1",3);
//        sb.functionalUnits.get(0).busy = true;
//        sb.insertFU("int","add1",1);
//        System.out.println("FU FP full: "+ sb.fullFuncUnit("FP"));
//        System.out.println("FU int full: "+ sb.fullFuncUnit("int"));
    }
   
}
