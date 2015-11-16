
public class Cache 
{
	private int[][] content;
	private int assoc;
	private int writePolicy; // write policy will be indicated by an int value to be discussed later
	private int cycles;
	
	
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
		content = new int[size/lineSize][lineSize];
		assoc = associativity;
	}
	
//	another constructor that takes all the needed parameters
	public Cache(int size, int lineSize, int associativity, int writePolicyParam, int cyclesParam) 
	{
		if (lineSize > size || cycles == 0) 
		{
			System.out.println("Error. Parameters are not correct");
			return;
		}
		writePolicy = writePolicyParam;
		cycles = cyclesParam;
		content = new int[size/lineSize][lineSize];
		assoc = associativity;
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
	
	public int[] getContentOf(int index) 
	{
		return this.content[index];
	}

}
