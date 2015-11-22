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
   // method that divides the address into tag,index,and offset
   public int[] divide(int address) {
	   String binary = Integer.toBinaryString(address);
	  /// System.out.println(binary.length());
	   for (int i = binary.length(); i< 32 ;i++) {
		   binary = "0" + binary;
	   }
	  // System.out.println("BinaryString: " + binary);
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
   }
}
