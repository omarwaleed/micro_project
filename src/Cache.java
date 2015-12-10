import java.util.Arrays;

import com.sun.xml.internal.bind.CycleRecoverable;



public class Cache 
{
	private String[][] content;
	private int assoc;
	private int writePolicy; // write policy will be indicated by an int value to be discussed later
	private int cycles;
	private int size;
	private int lineSize;
	private boolean mainMemory=false;//the default is the cache but if it is main set it by setMainMemory method
	private int hitRate = 0;
	int NoOfBlocks;
	int offsetBits;
	int indexBits;
	 //valid bit is stored at index content[n][content[n].length-1]
	//tag bit is stored at index content[n][content[n].length-2]
	
//	initialize the cache with given parameters
	public Cache(int size, int lineSize, int associativity) 
	{
		NoOfBlocks=size/lineSize;
//		checks if the line size is bigger than the cache size and stops the cache creation
		if (lineSize > size) 
		{
			System.out.println("Error. Parameters are not correct");
			return;
		}
		
//		size of each indexed value will depend on the line size since the cache has a fixed number of inputs
		this.size = size;
		this.lineSize = lineSize;
		content = new String[size/lineSize][lineSize+2];//one column for tag,one for valid bit
		assoc = associativity;
	}
	
//	another constructor that takes all the needed parameters
	public Cache(int size, int lineSize, int associativity, int writePolicyParam, int cyclesParam) 
	{
		NoOfBlocks=size/lineSize;
		
		if(associativity==1)//direct map
	    		indexBits = (int)(Math.log((NoOfBlocks))/Math.log(2));
		else if(associativity==NoOfBlocks){
			indexBits=0;
		}
		else{
			int No_of_sets=NoOfBlocks/associativity;
			 indexBits = (int)(Math.log((No_of_sets))/Math.log(2));
			
		}
	    
	    offsetBits = (int)(Math.log(lineSize)/Math.log(2));
		
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
		if(writePolicy==0)//write through
			content = new String[size/(lineSize/2)][lineSize+2];//tag will be stored and valid bit
		else //note that the last byte is the valid bit(write back)
			{
			//write back
				content = new String[size/(lineSize/2)][lineSize+3];//tag,valid and dirty bit
				for (int i = 0; i <content.length ; i++) {
					content[i][content[i].length-1]="0";//dirty bit
					content[i][content[i].length-2]="0";//valid bit
				}
			}
	}
	
	
//	setters and getters for what is to be modified after the creation of the cache if first constructor was used
	public int getWritePolicy() 
	{
		return writePolicy;
	}
	public boolean isMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(boolean mainMemory) {
		this.mainMemory = mainMemory;
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
	public void setDirtyBit(int blockNo){
		content[blockNo][content[0].length-1]="1";//some one modified this block
		
			
	}
	public boolean isDirty(int index) {
		return (content[index][content[index].length-1].equals("1"));
	}
 	public int getAssoc() {
		return assoc;
	}

	public String[][] getContent() {
		return content;
	}

	public void setContent(String[][] content) {
		this.content = content;
	}

	public void setAssoc(int assoc) {
		this.assoc = assoc;
	}

	public int getNoOFBlocks() {
		return NoOfBlocks;
	}

	public int getLineSize() {
		return lineSize;
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
	}

	public void setNoOFBlocks(int noOfBlocks) {
		NoOfBlocks = noOfBlocks;
	}

	public void clearDirtyBit(int blockNo){
		content[blockNo][content[0].length-1]="0";//the block is not valid 
	}
	// method to check if the data is already there
	public boolean hitOrMissDM(int address) {
		int[] division = divide(address);

		int blockNo = size / lineSize;
		// System.err.println("Division: "+ Arrays.toString(division));
		int index = divide(address)[1];
		// System.out.println("index in hitOrMiss " + index);
		int tag = divide(address)[0];
		// System.out.println("index " + index + " tag " + tag);
		 //System.err.println("index value " + index + " "+content.length );
		if (writePolicy == 0) {
			if (content != null && index < content.length && content[index] != null && content[index][content[index].length - 1] != null && content[index][content[index].length - 2] != null) {
				return !(content[index][content[index].length - 1].equals("0") || !content[index][content[index].length - 2].equals(tag + ""));
			}
		}
		if (writePolicy == 1) {
			if (content != null && index < content.length && content[index] != null && content[index][content[index].length - 2] != null && content[index][content[index].length - 3] != null) {
				return !(content[index][content[index].length - 2].equals("0") || !content[index][content[index].length - 3].equals(tag + ""));
			}


		}
		return false;
	}
	//method to read from cache 
   
   public String[] readDM(int address) {
	   int blockNo = size/lineSize;
       int index = divide(address)[1];
       System.err.println(divide(address)[0] + " " + content.length);
	   if (hitOrMissDM(address)) {
              hitRate++;
           System.err.println(Arrays.toString(getContentOf(index)));
		   return getContentOf(index);
	   }
	  return null;
   }
   //method to write to the cache
   public void writeDM(int address,  String[] data) { // bug in the tag and index use the binary thing
	   //contentString();
	  
	   int blockNo = size/lineSize;
	   int index = divide(address)[1];
	   int offset = getOffset();
	 //  System.err.println("Hit? : " + hitOrMissDM(address) + " block number " + blockNo);
	   if (hitOrMissDM(address)) { // hit
		   //content[index] = data;

		   hitRate++;
		 
		   if (writePolicy == 1) { // write back
			   int tag = divide(address)[0];
			   String[] newData = new String[data.length+3];
			   for(int i = 0; i<data.length;i++) {
				   newData[i] = data[i];
			   }
			   newData[data.length] = tag+"";
			   newData[data.length+1] = "1";
			   newData[data.length+2] = "1"; // set dirty bit

			   //  System.err.println(Arrays.toString(newData));
			   content[index] = newData; // replace
		   }
		   if (writePolicy == 0) {
			   // write through method here
			   int tag = divide(address)[0];
			   String[] newData = new String[data.length+2];
			   for(int i = 0; i<data.length;i++) {
				   newData[i] = data[i];
			   }
			   newData[data.length] = tag+"";
			   newData[data.length+1] = "1";
			   //  System.err.println(Arrays.toString(newData));
			   content[index] = newData;
			   Processor.writeBackOrThrought(this,Processor.getPhysicalAddressi(index,this),index);

		   }
	   }
	   else { // miss
		   if (writePolicy == 1) { // write back
			   // write back method here
			   if (isDirty(index))
				   Processor.writeBackOrThrought(this, Processor.getPhysicalAddressi(index, this), index);
			   int tag = divide(address)[0];
			   String[] newData = new String[data.length+3];
			   for(int i = 0; i<data.length;i++) {
				   newData[i] = data[i];
			   }
			   newData[data.length] = tag+"";
			   newData[data.length+1] = "1";
			   System.err.println(Arrays.toString(newData));
			   newData[data.length+2] = "0";
			   System.err.println(Arrays.toString(newData));
			   if (index < content.length)
				   content[index] = newData; // replace
			   
		   } else { // write through

			   int tag = divide(address)[0];
			   String[] newData = new String[data.length + 2];
			   for (int i = 0; i < data.length; i++) {
				   newData[i] = data[i];
			   }
			   newData[data.length] = tag + "";
			   newData[data.length + 1] = "1";
			   System.err.println(Arrays.toString(newData));
			   if (index < content.length)
				   content[index] = newData; // replace
		   }
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
			indexBits = (int)(Math.log((size/(getLineSize())))/Math.log(2));
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
		}else if(assoc>1 && assoc <NoOfBlocks){
			int indexBits = (int) ((int)(Math.log(NoOfBlocks/assoc))/Math.log(2))+1;
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
	public String[] read(int address) {
		if (assoc == 1) // direct mapped cache
			return readDM(address);
		else // set and full assoc cache
			return readsetandfull(address);
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
		if(assoc>1 && assoc< NoOfBlocks){
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
		else if(assoc== NoOfBlocks){
			System.out.println("ana d5lt hena");
			int[]divide = divide(address);
			int tag=divide(address)[0];
			for(int i= 0; i<NoOfBlocks; i++){
				if(content!= null && content[i]!=null && content[i][content[i].length-1] != null && content[i][content[i].length-2]!=null ){
					if(content[i][content[i].length-1].equals("1") && content[i][content[i].length-2].equals(tag+"")){
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
		if(assoc>1 && assoc< NoOfBlocks){
			if(hitormiss(address)!=-3){
				content[hitormiss(address)] = data;
				hitRate++;
//if miss then we will check if there is an empty block then we will replace it in this block
			}else if (assoc == 1) { // direct mapped cache
				writeDM(address,data);
			}
			else{ // fully associative cache
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
					if(content[i][content[i].length-2]==null){
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
		}else if(assoc== NoOfBlocks){
			// if there is a hit
			if(hitormiss(address)!=-3){
				content[hitormiss(address)]=data;
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
				for(int i=0 ; i<NoOfBlocks; i++){
					if(content[i][content[i].length-2]==null){
						content[i] = newData; // replace
						added=true;
						break;

					}
				}
				if(added=false){
					int random= 0+ (int)(Math.random()*NoOfBlocks);
					content[random]=newData;
				}
			}
		}
	}

}
