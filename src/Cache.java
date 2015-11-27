
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
	private int numofblocks = (int)size/lineSize;
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
		this.lineSize = lineSize;
		content = new String[size/(lineSize/2)][lineSize];
		assoc = associativity;
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
		this.size = size;
		this.lineSize = lineSize;
		if (assoc ==1) {		
		 content = new String[size/(lineSize/2)][lineSize+2];
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
        int blockNo = size/lineSize;
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
	   int blockNo = size/lineSize;
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
	  
	   int blockNo = size/lineSize;
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
	   content = new String[size/lineSize][lineSize+2];
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
	   int indexBits = (int)(Math.log((size/(lineSize)))/Math.log(2))+1;
	   int offsetBits = (int)(Math.log(lineSize)/Math.log(2));
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
	   }else if(assoc>1 && assoc <numofblocks){
		   int indexBits = (int) ((int)(Math.log(numofblocks/assoc))/Math.log(2))+1;
		   int offsetBits = (int)(Math.log(lineSize)/Math.log(2));
		   int tagBits = 32-indexBits-offsetBits;
		   	//   System.out.println("Index bits: " + indexBits + " Offset bits: " + offsetBits + " Tag bits: " + tagBits);
		   	   int offset = Integer.parseInt(binary.substring(binary.length()-offsetBits, binary.length()),2);
		   	 //  System.out.println("Offset: " + offset);
		   	   int tag = Integer.parseInt(binary.substring(0,binary.length()-offsetBits-indexBits),2);
		   	 //  System.out.println("Tag: " + tag);
		   	   int index = Integer.parseInt(binary.substring(binary.length()-(offsetBits+indexBits),binary.length()-offsetBits),2);
		   	  // System.out.println("index value: " + index);
		   	  int []division ={tag,index,offset};
		   	  return division;
		   	  // if it is fully associative
	   }else{
		  int offsetBits =  (int)(Math.log(lineSize)/Math.log(2));
		  int tagBits= 32-offsetBits;
		  int offset = Integer.parseInt(binary.substring(binary.length()-offsetBits, binary.length()),2);
		  int tag = Integer.parseInt(binary.substring(0,binary.length()-offsetBits),2);
		  int[]division={tag,offset};
		  return division;
	   }
	   
   }
   // to detect if hit or miss in set associative
   public int hitormissset(int address){
   int[]divide = divide(address);
   int tag=divide(address)[0]; 
   int index= divide(address)[1];  // which will determine the set number in this case
   int startreadblock = index * assoc ; // this is the first block within the set we have
   for(int i=startreadblock ; i<=startreadblock+assoc ; i++){
   if(content!= null && content[i]!=null && content[i][content[i].length-1] != null && content[i][content[i].length-2]!=null ){
   // check if the valid bit is equal to 1 and our tag is equal to the tag in the cache block.
   if(content[i][content[i].length-1].equals("1") && content[i][content[i].length-2].equals(tag+"")){
   // if hit return the index of the hit block in the set and -3 if miss 
   return i;
   }
   }

   }
   return -3;
   }
    
   public int hitormissfull(int address){
	   int[]divide = divide(address);
	   int tag=divide(address)[0];
	   for(int i= 0; i<numofblocks; i++){
		   if(content!= null && content[i]!=null && content[i][content[i].length-1] != null && content[i][content[i].length-2]!=null ){
			 if(content[i][content[i].length-1].equals("1") && content[i][content[i].length-2].equals(tag+"")){
			   return i;
			   }
			   }

			   }
			   return -3;
	   }
   
public int createrandom(int startreadblock){
	int endblock= startreadblock+assoc;
	int num = startreadblock+ (int)(Math.random()*endblock);
	return startreadblock;
}
public void writefull(int address, String[]data){
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
	for(int i=0 ; i<numofblocks; i++){
		if(content[i][content[i].length-2].equals("")){
			   content[i] = newData; // replace 
			   added=true;
			   break;
			   
		}
	}
	if(added=false){
		int random= 0+ (int)(Math.random()*numofblocks);
		content[random]=newData;
	}
}
}
   public void writeset(int address , String[]data){
// if there is hit (tag equals tag) then we will replace the content of this block to the data
if(hitormissset(address)!=-3){
content[hitormissset(address)] = data;
hitRate++;
//if miss then we will check if there is an empty block then we will replace it in this block
}else{
int[]divide = divide(address);
   int tag=divide(address)[0]; 
   int index= divide(address)[1];  // which will determine the set number in this case
   int startreadblock = index * assoc ;// this is the first block within the set we have
   // added checks if the content is put in an empty block or we have to replace.
   String[] newData = new String[data.length+2];
   boolean added=false;
   for(int i = 0; i<data.length;i++) {
	   newData[i] = data[i];
   }
	newData[data.length] = tag+"";
	   newData[data.length+1] = "1";
   for(int i=startreadblock ; i<=startreadblock+assoc ; i++){
if(content[i][content[i].length-2].equals("")){
	   content[i] = newData; // replace 
	added=true;
	break;
}
   }
   if(added=false){
	   // insert the block in a random number within the set
	  int random= createrandom(startreadblock);
	  content[random]=newData;
   }
}
   }

public void main (String[]args){
	Cache b = new Cache();
}
}


