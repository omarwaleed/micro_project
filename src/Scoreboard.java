import java.util.Hashtable;

public class Scoreboard {
   static Scoreboard sb;
   Hashtable<String,String> registerStatus;
   Entry[] rob;  // reorder buffer is an array of entries 
   int head;  // rob head pointer
   int tail;  // rob tail pointer
      // add reservation station here 
   private Scoreboard(){
	   registerStatus = new Hashtable<String,String>();
	   rob = new Entry[10];
	   head = tail = 0;
   }
   public static Scoreboard getInstance() {  // use this method to get access the score board
	   if (sb == null) 
		   sb = new Scoreboard();
	   return sb;
   }
  public int increment(int i) {  // use this method to increment either the head or the tail and pass to it the int you want to increment
	  if (i == 9) 
		  return 0;
	      return i++;
  }
  public boolean robFull() {
	 for(int i = 0; i < rob.length;i++) {
		 if (!rob[i].isOccupied())
			 return false;
	 }
	   return true;
  }
   
}
