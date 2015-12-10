
import java.util.Arrays;


public class Cache 
{
	private String[][] content;
	private int assoc;
	private int writePolicy; // write policy will be indicated by an int value to be discussed later
	private int cycles;
	private int size;
	private int lineSize;
	private int hitRate = 0;
	int indexBits;
	int offsetBits;
	int NoOFBlocks;
	 //valid bit is stored at index content[n][content[n].length-1]
	//tag bit is stored at index content[n][content[n].length-2]
	
//	initialize the cache with given parameters
	public Cache(int size, int lineSize, int associativity) 
	{
//		checks if the line size is bigger than the cache size and stops the cache creation
		if (lineSize > size) 
		{
			System.out.println("Error. Parameters are not correct");
			return;
		}
		
//		size of each indexed value will depend on the line size since the cache has a fixed number of inputs
		this.size = size;
		this.setLineSize(lineSize);
		content = new String[size/lineSize][lineSize];
		assoc = associativity;
		NoOFBlocks= (int) size/lineSize;
	}
	
//	another constructor that takes all the needed parameters
	public Cache(int size, int lineSize, int associativity, int writePolicyParam, int cyclesParam) 
	{
		if (lineSize > size || cyclesParam == 0) 
		{
			System.out.println(size + " " + lineSize + " " + associativity + " " + writePolicyParam + " " + cyclesParam);
			System.out.println("Error. Parameters are not correct");
			return;
		}
		writePolicy = writePolicyParam;
		cycles = cyclesParam;
		assoc = associativity;
		NoOFBlocks= (int) size/lineSize;
		this.size = size;
		this.setLineSize(lineSize);
		if (writePolicy == 0) {		
		 content = new String[size/lineSize][lineSize+2];
		}
		if (writePolicy == 1) {
			content = new String[size/lineSize][lineSize+3];
		}
	}
	
	
//	setters and getters for what is to be modified after the creation of the cache if first constructor was used
	public int getWritePolicy() 
	{
		return writePolicy;
	}

	public void setWritePolicy(int writePolicy) 
	{
		this.writePolicy = writePolicy;
	}

	public int getCycles() 
	{
		return cycles;
	}

	public void setCycles(int cycles) 
	{
		this.cycles = cycles;
	}
	
	public String[] getContentOf(int index) 
	{
		return this.content[index];
	}
	// method to check if the data is already there
	public boolean hitOrMissDM(int address) {
		//int[] division = divide(address);
        int blockNo = size/getLineSize();
       // System.err.println("Division: "+Arrays.toString(division));
        int index =  divide(address)[1];
       // System.out.println("index in hitOrMiss " + index);
        int tag =  divide(address)[0];
       // System.out.println("index " + index + " tag " + tag);
       // System.err.println("index value " + index + " "+content.length );
         if (content != null && content[index] != null && content[index][content[index].length-1] != null && content[index][content[index].length-2] != null){
        if (content[index][content[index].length-1].equals("0") || !content[index][content[index].length-2].equals(tag+""))
		  return false;
        else 
        	return true;
         }
         return false;
            
	}
	//method to read from cache 
   public String[] readDM(int address) {
	   int blockNo = size/getLineSize();
       int index = divide(address)[1];
      
	   if (hitOrMissDM(address)) {
              hitRate++;
           System.out.println(Arrays.toString(getContentOf(index)));
		   return getContentOf(index);
	   }
	  return null;
   }
   //method to write to the cache
   public void writeDM(int address,String[] data) { // bug in the tag and index use the binary thing
	   //contentString();
	  
	   int blockNo = size/getLineSize();
	   int index = divide(address)[1];
	   int offset = getOffset();
	 //  System.err.println("Hit? : " + hitOrMissDM(address) + " block number " + blockNo);
	   if (hitOrMissDM(address)) {
		   content[index] = data; 
		   hitRate++;
		 
		   if (writePolicy == 1) {
			   // set the dirty bit here
		   }
		   if (writePolicy == 2) {
			   // write through method here
		   }
	   }
	   else {
		   if (writePolicy == 1) {
			   // write back method here 
			   
		   }
		   if (writePolicy == 2) {
			   // write through method here
		   }
		   int tag = divide(address)[0];
		   String[] newData = new String[data.length+2];
		   for(int i = 0; i<data.length;i++) {
			   newData[i] = data[i];
		   }
		   newData[data.length] = tag+"";
		   newData[data.length+1] = "1";
		//  System.err.println(Arrays.toString(newData));
		   content[index] = newData; // replace 
		   
	   }
	   
   }
   public int getOffset() {
	   
	   return 0;
   }
   public int getHitRate() {
	   return hitRate;
   }
   public void clearCache() {
	   content = new String[size/getLineSize()][getLineSize()+2];
   }
   public void contentString() {
	   for(int i = 0; i<content.length;i++) {
		   for(int j = 0; j < content[i].length;j++) {
			   if (j == 0)
				   System.out.print("["+content[i][j]);
			   else if (j == content[0].length-1) 
				   System.out.print(", "+content[i][j]+"]");
			   else 
				   System.out.print(", " + content[i][j]);		   
		   }
		   System.out.println();
	   }
	   
   }
   // method that divides the address into tag,index,and offset for direct mapping 
   public int[] divide(int address) {
	   String binary = Integer.toBinaryString(address);
	  /// System.out.println(binary.length());
	   for (int i = binary.length(); i< 32 ;i++) {
		   binary = "0" + binary;
	   }
	  // System.out.println("BinaryString: " + binary);
	   // assoc = 1 means that it is direct mapping 
	   
	   if(assoc==1){
	   indexBits = (int)(Math.log((size/(getLineSize())))/Math.log(2))+1;
	   offsetBits = (int)(Math.log(getLineSize())/Math.log(2));
	   int tagBits = 32-(indexBits+offsetBits);
	//   System.out.println("Index bits: " + indexBits + " Offset bits: " + offsetBits + " Tag bits: " + tagBits);
	   int offset = Integer.parseInt(binary.substring(binary.length()-offsetBits, binary.length()),2);
	 //  System.out.println("Offset: " + offset);
	   int tag = Integer.parseInt(binary.substring(0,binary.length()-offsetBits-indexBits),2);
	 //  System.out.println("Tag: " + tag);
	   int index = Integer.parseInt(binary.substring(binary.length()-(offsetBits+indexBits),binary.length()-offsetBits),2);
	  // System.out.println("index value: " + index);
	   int[] division = {tag,index,offset};
	   return division;
	   // if it is set associative
	   }else if(assoc>1 && assoc <NoOFBlocks){
		   int indexBits = (int) ((int)(Math.log(NoOFBlocks/assoc))/Math.log(2))+1;
		   int offsetBits = (int)(Math.log(getLineSize())/Math.log(2));
		   int tagBits = 32-indexBits-offsetBits;
		   	  // System.out.println("Index bits: " + indexBits + " Offset bits: " + offsetBits + " Tag bits: " + tagBits);
		   	   int offset = Integer.parseInt(binary.substring(binary.length()-offsetBits, binary.length()),2);
		   	  //System.out.println("Offset: " + offset);
		   	   int tag = Integer.parseInt(binary.substring(0,binary.length()-offsetBits-indexBits),2);
		   	  // System.out.println("Tag: " + tag);
		   	   int index = Integer.parseInt(binary.substring(binary.length()-(offsetBits+indexBits),binary.length()-offsetBits),2);
		   	  // System.out.println("index value: " + index);
		   	  int []division ={tag,index,offset};
		   	  return division;
		   	  // if it is fully associative
	   }else{
		  int offsetBits =  (int)(Math.log(getLineSize())/Math.log(2));
		  int tagBits= 32-offsetBits;
		  int offset = Integer.parseInt(binary.substring(binary.length()-offsetBits, binary.length()),2);
		  int tag = Integer.parseInt(binary.substring(0,binary.length()-offsetBits),2);
		  int[]division={tag,offset};
		  return division;
	   }
	   
   }
   
   public String[] readsetandfull(int address) {
      int hitindex= hitormiss(address);
	   if (hitindex!=-3) {  // this means hit
              hitRate++;
           System.out.println(Arrays.toString(getContentOf(hitindex)));
		   return getContentOf(hitindex);
	   }
	  return null;
   }
   
   // to detect if hit or miss for both set and fully associative
   public  int hitormiss(int address){
	   if(assoc>1 && assoc< NoOFBlocks){
   int[]divide = divide(address);
   int tag=divide(address)[0]; 
   int index= divide(address)[1];  // which will determine the set number in this case
   int startreadblock = index * assoc ; // this is the first block within the set we have
  /* System.out.println("tag "+ tag);
   System.out.println("index"+ index);
   System.out.println("start block "+ startreadblock);
   System.out.println();
   System.out.println();*/
   for(int i=startreadblock ; i<startreadblock+assoc ; i++){
	 //  System.out.println("startblock "+ startreadblock);
   if(content!= null && content[i]!=null && content[i][content[i].length-1] != null && content[i][content[i].length-2]!=null ){
   // check if the valid bit is equal to 1 and our tag is equal to the tag in the cache block.
	 /*  System.out.println(i+" "+content[i][content[i].length-2]);
	   System.out.println("My tag"+ tag);*/
   if(writePolicy==1 && content[i][content[i].length-2].equals("1") && content[i][content[i].length-3].equals(tag+"")){
   // if hit return the index of the hit block in the set and -3 if miss 
	 // System.out.println("ana hena yabn el 7alal");
   return i;
   }else if(writePolicy==0 && content[i][content[i].length-1].equals("1") && content[i][content[i].length-2].equals(tag+"")){
	   return i;
   }
   }

   }
   return -3;
   }
   else if(assoc== NoOFBlocks){
	  // System.out.println("ana d5lt hena");
	   int[]divide = divide(address);
	   int tag=divide(address)[0];
	   for(int i= 0; i<NoOFBlocks; i++){
		   if(content!= null && content[i]!=null && content[i][content[i].length-1] != null && content[i][content[i].length-2]!=null ){
			   if( writePolicy==1 && content[i][content[i].length-2].equals("1") && content[i][content[i].length-3].equals(tag+"")){
			   return i;
			   }else if (writePolicy==0 && content[i][content[i].length-1].equals("1") && content[i][content[i].length-2].equals(tag+"")){
				   return i;
			   }
			   }

			   }
			   return -3;
   }else{
	   return -3;
   }
}

    
  /*public int hitormissfull(int address){
	   int[]divide = divide(address);
	   int tag=divide(address)[0];
	   for(int i= 0; i<NoOFBlocks; i++){
		   if(content!= null && content[i]!=null && content[i][content[i].length-1] != null && content[i][content[i].length-2]!=null ){
			 if(content[i][content[i].length-1].equals("1") && content[i][content[i].length-2].equals(tag+"")){
			   return i;
			   }
			   }

			   }
			   return -3;
	   }*/
   
public int createrandom(int startreadblock){
	int endblock= startreadblock+assoc;
	int num = startreadblock+ (int)(Math.random()*endblock);
	return startreadblock;
}
/*public void writefull(int address, String[]data){
	// if there is a hit
	if(hitormissfull(address)!=-3){
		content[hitormissfull(address)]=data;
		hitRate++;
	}
else{
	 int tag = divide(address)[0];
		   String[] newData = new String[data.length+2];
		   boolean added=false;
		   for(int i = 0; i<data.length;i++) {
			   newData[i] = data[i];
		   }
			newData[data.length] = tag+"";
			newData[data.length+1] = "1";
	for(int i=0 ; i<NoOFBlocks; i++){
		if(content[i][content[i].length-2]==null){
			   content[i] = newData; // replace 
			   added=true;
			   break;
			   
		}
	}
	if(added=false){
		int random= 0+ (int)(Math.random()*NoOFBlocks);
		content[random]=newData;
	}
}
}*/
   public void write(int address , String[]data){
// if there is hit (tag equals tag) then we will replace the content of this block to the data
	 // System.out.println(NoOFBlocks);
	   if(assoc>1 && assoc< NoOFBlocks){
		   int[]divide = divide(address);
		   int tag=divide(address)[0]; 
		   int index= divide(address)[1];  // which will determine the set number in this case
		   int startreadblock = index * assoc ;// this is the first block within the set we have
		   //System.out.println("ana hena");
if(hitormiss(address)!=-3){ // hit
	// writepolicy 0 -> writethrough
	//writepolicy 1 -> writeback
	
	if(writePolicy==1){ //write back 
		String[]newData = new String[data.length+3];
		for(int i = 0; i<data.length;i++) {
			   newData[i] = data[i];
		   }
			newData[data.length] = tag+""; // set new data (newdata.length) to tag;
			   newData[data.length+1] = "1"; // set valid bit to 1
		newData[data.length+2]= "1"; // set dirty bit to 1
content[hitormiss(address)] = newData;

	}else   //writethrough
	{
		String[]newData = new String[data.length+2];
		for(int i = 0; i<data.length;i++) {
			   newData[i] = data[i];
		   }
		//System.out.println("ana hena");
			newData[data.length] = tag+"";
			   newData[data.length+1] = "1";
			   
			   content[hitormiss(address)] = newData;
			   
		Processor.writeBackOrThrought(this, Processor.getPhysicalAddressi(index, this), index);
		
	}
hitRate++;
//if miss then we will check if there is an empty block then we will replace it in this block
}else{ // miss

   // added checks if the content is put in an empty block or we have to replace.

   boolean added=false;
   if(writePolicy==1){
	   String[] newData = new String[data.length+3];
	   int variable=0;
		newData[data.length] = tag+"";
		   newData[data.length+1] = "1";
		   newData[data.length+2]="0";
		//   System.out.println("index "+index);
		 
		   for(int i = 0; i<data.length;i++) {
			   newData[i] = data[i];
		   }
		
   for(int i=startreadblock ; i<=startreadblock+assoc ; i++){
      
if(content[i][content[i].length-2]==null){
	//System.out.println("in the if condition"); 
	   content[i] = newData; // replace 
	added=true;
	variable=i;
	break;
}

   }
   //System.err.println("content b3d el loop: " + Arrays.toString(content[variable]));
   
   if(added==false){
	   // insert the block in a random number within the set
	  int random= createrandom(startreadblock);
	  variable=random;
	  content[random]=newData;
   }
   //Processor.get
   //System.out.println("test "+Processor.getPhysicalAddressi(variable, this));
   Processor.writeBackOrThrought(this, Processor.getPhysicalAddressi(variable, this), variable);
}else //writethrough
{
	//System.out.println("ana hena");
	   String[] newData = new String[data.length+2];
	   for(int i = 0; i<data.length;i++) {
		   newData[i] = data[i];
	   }
		newData[data.length] = tag+"";
		   newData[data.length+1] = "1";
		 // System.out.println("ana hena");
		   
	   for(int i=startreadblock ; i<=startreadblock+assoc ; i++){
		   content[i] = new String[data.length+1];
		   
	if(content[i][content[i].length-2]==null){
		   content[i] = newData; // replace 
		added=true;
		break;
	}
		   }
	   
	   if(added==false){
		   // insert the block in a random number within the set
		  int random= createrandom(startreadblock);
		  content[random]=newData;
	   }	
}
}
	   }else if(assoc== NoOFBlocks){ //fullllllly associative
			// if there is a hit
		   int tag = divide(address)[0];
		   
			if(hitormiss(address)!=-3){
				int hitblock= hitormiss(address);
				if(writePolicy==1){ // write back full
					String[]newData = new String[data.length+3];
					for(int i = 0; i<data.length;i++) {
						   newData[i] = data[i];
					   }
						newData[data.length] = tag+""; // set new data (newdata.length) to tag;
						   newData[data.length+1] = "1"; // set valid bit to 1
					newData[data.length+2]= "1"; // set dirty bit to 1
			content[hitormiss(address)] = newData;

				
			}else{ //writethrough full
				String[]newData = new String[data.length+2];
				for(int i = 0; i<data.length;i++) {
					   newData[i] = data[i];
				   }
					newData[data.length] = tag+"";
					   newData[data.length+1] = "1"; // valid bit to 1
					   
					   content[hitormiss(address)] = newData;
					   
				Processor.writeBackOrThrought(this, Processor.getPhysicalAddressi(hitblock, this), hitblock);
				
			}
				hitRate++;
			}
		else{  //miss
			 if(writePolicy==1){ //writeback
				   String[] newData = new String[data.length+3];
				   boolean added=false;
				   int variable=0;
				   for(int i = 0; i<data.length;i++) {
					   newData[i] = data[i];
				   }
					newData[data.length] = tag+"";
					newData[data.length+1] = "1";
					newData[data.length+2] ="1";
					
			for(int i=0 ; i<NoOFBlocks; i++){
				if(content[i][content[i].length-2]==null){
					   content[i] = newData; // replace 
					   added=true;
					   variable=i;
					   break;
					   
				}
			}
			if(added==false){
				int random= 0+ (int)(Math.random()*NoOFBlocks);
				variable=random;
				content[random]=newData;
			}
			Processor.writeBackOrThrought(this, Processor.getPhysicalAddressi(variable, this), variable);
		}else{  //write through
			   String[] newData = new String[data.length+2];
			   boolean added=false;
			   int variable=0;
			   for(int i = 0; i<data.length;i++) {
				   newData[i] = data[i];
			   }
				newData[data.length] = tag+"";
				newData[data.length+1] = "1";
				
		for(int i=0 ; i<NoOFBlocks; i++){
			if(content[i][content[i].length-2]==null){
				   content[i] = newData; // replace 
				   added=true;
				   variable=i;
				   break;
				   
			}
		}
		if(added==false){
			int random= 0+ (int)(Math.random()*NoOFBlocks);
			variable=random;
			content[random]=newData;
		}
			
		}
		}
	   }
   }


public void setContent(String[][] content) {
	this.content = content;
}

public int getAssoc() {
	return assoc;
}
public String[][] getContent() {
	return content;
}

public void setAssoc(int assoc) {
	this.assoc = assoc;
}

public int getNoOFBlocks() {
	return NoOFBlocks;
}

public void setNoOFBlocks(int NoOFBlocks) {
	this.NoOFBlocks = NoOFBlocks;
}

public int getLineSize() {
	return lineSize;
}

public void setLineSize(int lineSize) {
	this.lineSize = lineSize;
}
public static void main (String[]args){
	Cache c= new Cache(32,4,8,1,2);
	String[]habal= {"8","9"};
	/*System.out.println(c.hitormiss(1));
	c.write(1,habal);	*/
	System.out.println(c.hitormiss(8));
	c.write(8, habal);
//System.out.println(c.hitormiss(6));
//c.write(6,habal);
System.out.println(c.hitormiss(6));
c.write(6, habal);
System.out.println(c.hitormiss(9));
if(c.readsetandfull(6)!=null){
	System.out.println(c.readsetandfull(6).toString());
}
	
}

}
