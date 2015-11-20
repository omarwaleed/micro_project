import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// this class handles the processor functions and contains all the processor registers

public class Processor {
	
//	Initialize the registers and the cache levels
//	cache levels will end with the memory level but to be handled after user input
	private static int[] register = new int[32];
	private static ArrayList <Cache> cacheLevel = new ArrayList<Cache>();

//	get the value inside a single register
	public int getRegister(int reg) 
	{
		return register[reg];
	}

//	set the value of a single register
	public void setRegister(int reg, int value) 
	{
//		checks on an attempt to modify register 0 if so ignore the setting
		if (reg != 0) 
		{
			register[reg]= value;
		}
	}
	
//	get all registers
	public int[] allRegisters()
	{
		return register;
	}
	public void writeBackOrThrought(Cache c ,int physicaladdress,int index){
		String []tmp=c.getContentOf(index);
		for (int i = 0; i < cacheLevel.size(); i++) {
			Cache c2=cacheLevel.get(i);
			if(c2.getAssoc()==1){//direct or set associative
			int index2=physicaladdress%c.getNoOFBlocks();
			int tag=physicaladdress/c.getNoOFBlocks();
			if(c2.getWritePolicy()==0){//write through
				if(c2.getContent()[index2][c2.getContent()[index2].length-1].equals(String.valueOf(tag))){
					for (int j = 0; j < c2.getContent()[index].length-1; j++) {
						c2.getContent()[index2][i]=tmp[i];
					}
					
				}
				
			}
			else{//write back
				int index21=physicaladdress%c.getNoOFBlocks();
				int tag1=physicaladdress/c.getNoOFBlocks();
				if(c2.getWritePolicy()==0){//write through
					if(c2.getContent()[index21][c2.getContent()[index21].length-2].equals(String.valueOf(tag1))){
						for (int j = 0; j < c2.getContent()[index].length-2; j++) {
							c2.getContent()[index21][i]=tmp[i];
						}
						
					}
				
				
			}
			}}
			
			else if(c2.getAssoc()==c2.getNoOFBlocks()){//full associative
				int index3=-1 ;
				for (int j = 0; j < c2.getContent().length; j++) {
					if(c2.getContent()[j][c2.getContent()[j].length-1].equals(String.valueOf(index))){
						index3=j;
								break;
					}
					
				}
				if(c2.getWritePolicy()==0&&index3!=-1){
					for (int j = 0; j < c2.getContent()[index].length-1; j++) {
							c2.getContent()[index3][i]=tmp[i];
						}
						
					
				}
				
				else if(index3!=-1){//write back
					
					for (int j = 0; j < c2.getContent()[index].length-2; j++) {
						c2.getContent()[index3][i]=tmp[i];
					}
			
				
			}
			
			
		}
			else{//set associative 
				int set_no=physicaladdress%c2.getAssoc();
				int tag=physicaladdress/c2.getAssoc();
				int startBlock=set_no*c2.getNoOFBlocks();
				int index3=-1;
			
				for (int j =0; j < (c2.getNoOFBlocks()/c2.getAssoc()); j++) {
					if(c2.getContent()[startBlock][c2.getContent()[0].length-1].equals(String.valueOf(tag))){
						index3=startBlock;break;
					}
					++startBlock;
				}
				////////////////
				if(c2.getWritePolicy()==0){
					for (int j = 0; j < c2.getContent()[startBlock].length-1; j++) {
						c2.getContent()[startBlock][j]=tmp[j];
						
					}
				}
				else{
					for (int j = 0; j < c2.getContent()[startBlock].length-2; j++) {
						c2.getContent()[startBlock][j]=tmp[j];
						
					}
					
				}
				///////////////////////
				
				
			}
		}
		
	}
	
	public static void main(String[] args) 
	{
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("What is the number of caches");
		
//		take the number of caches first
		int numberOfCaches;
		try {
			numberOfCaches = Integer.parseInt(bf.readLine());
		} catch (Exception e) 
		{
			System.out.println("Input error");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Now for every cache what are the parameters?");
		System.out.println("Format: Size,Line Size, Associativity");
		
//		use the number of caches given in the first line to create multiple caches with the given structure
		for (int i = 0; i < numberOfCaches; i++) 
		{
			String[] line;
			try {
//				input structure assumed is
//				int,int,int NOT (int,int,int)
//				Another format could be used which takes 5 parameters instead of 3
				
//				clear all spaces
				line = bf.readLine().replaceAll("\\s","").split(",");

				int [] parsedLine = new int [line.length];
				for (int j = 0; j < line.length; j++) 
				{
					parsedLine[j] = Integer.parseInt(line[j]);
				}
				
//				this will change is the 5 parameter constructor was used
//				this adds a new cache to the processor's arraylist of caches
				cacheLevel.add(new Cache(parsedLine[0], parsedLine[1], parsedLine[2]));
				
			} catch (IOException e) {
				System.out.println("Error when reading cache line");
				e.printStackTrace();
			}
		}
		
//		add the memory level after all caches are created
		System.out.println("What is the memory access time?");
		int memoryTime;
		try {
			memoryTime = Integer.parseInt(bf.readLine());
		} catch (Exception e) {
			System.out.println("Wrong input");
			e.printStackTrace();
			return;
		}
		
		cacheLevel.add(new Cache(64*1024, 16, 1, 0, memoryTime));
		
		
	}
	 

}
