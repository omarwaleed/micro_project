
public class Cache 
{
	private String[][] content;
	private int assoc;
	private int writePolicy; // write policy will be indicated by an int value to be discussed later
	//0=>write through ,1=>write back
	private int cycles;
	private boolean mainMemory=false;//the default is the cache but if it is main set it by setMainMemory method
	
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
		content = new String[size/lineSize][lineSize+1];//i added 1 to store the tag
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
		if(writePolicy==0)//write through
			content = new String[size/lineSize][lineSize+1];//tag will be stored at linesize
		else //note that the last byte is the valid bit(write back)
			{
				content = new String[size/lineSize][lineSize+2];//tag will be stored at linesize and valid at linesize+1
				for (int i = 0; i <content.length ; i++) {
					content[i][lineSize]="0";
				}
			}
		assoc = associativity;
	}
	
	
//	setters and getters for what is to be modified after the creation of the cache if first constructor was used
	public int getWritePolicy() 
	{
		return writePolicy;
	}
	public void setMainMemory(){
		mainMemory=true;
	}
	public void setValidBit(int blockNo){
		content[blockNo][content[0].length-1]="1";//some one modified this block
	}
	public void clearValidBit(int blockNo){
		content[blockNo][content[0].length-1]="0";//the block is not valid 
	}
	public void setWritePolicy(int writePolicy) 
	{
		this.writePolicy = writePolicy;
	}
	public void writeThrough(int blockNo){
		
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

}
