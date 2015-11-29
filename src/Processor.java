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
	/////soha#write through
	////method that take block no in a cache and return block No in MM
	public static int getPhysicalAddressi(int BlockNo,Cache c){
		String tmp;
		if(c.getAssoc()==c.getNoOFBlocks()){//full associative
			if(c.getWritePolicy()==0){
			tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-2]));
					
					//+Integer.toBinaryString(c.offsetBits);
			}
			else{
				tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-3]));
						//Integer.toBinaryString(c.offsetBits);;	
			}
			
			
			
		}
		else if(c.getAssoc()==1){//direct map
			String index=Integer.toBinaryString(BlockNo);
			while(index.length()<c.indexBits){
				index="0"+index;
			}
			System.out.println("index is "+ index);
			System.out.println(c.getContent()[BlockNo][c.getContent()[BlockNo].length-2]);
			if(c.getWritePolicy()==0){
				
				tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-2]))+index;
						//Integer.toBinaryString(c.offsetBits);
				//tmp2=(Integer.parseInt(c.getContent()[BlockNo][c.getContent().length-2])/(1-(1/c.getNoOFBlocks())*c.getNoOFBlocks()));
				}
				else{
					//tmp2=(Integer.parseInt(c.getContent()[BlockNo][c.getContent().length-3])/(1-(1/c.getNoOFBlocks())*c.getNoOFBlocks()));

					
					tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-3]))+index;
							//Integer.toBinaryString(c.offsetBits);;	
				}
			
		}
		else{//set associative
			String index=Integer.toBinaryString(BlockNo/c.getAssoc());
			while(index.length()<c.indexBits){
				index="0"+index;
			}
			if(c.getWritePolicy()==0){
				
				System.out.println(c.getContent()[BlockNo][c.getContent()[BlockNo].length-2]+" block is "+BlockNo);
				System.out.println(index);
				tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-2]))+index;
						
						//Integer.toBinaryString(c.offsetBits);
				
				}
				else{
					System.out.println(c.getContent()[BlockNo][c.getContent()[BlockNo].length-3]+" block is "+BlockNo);
					System.out.println(index);
					tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-3]))+
							index;
							//Integer.toBinaryString(c.offsetBits);	
				}
			
		}
		System.out.println("tmp is "+tmp);
		return Integer.parseInt(tmp,2);
		
		
	}
	
	//this method update the block in all level of caches and the main memory
	public  static void writeBackOrThrought(Cache c ,int physicaladdress,int index){
		String []tmp=c.getContentOf(index);
		for (int i = 0; i < cacheLevel.size(); i++) {
			
			Cache c2=cacheLevel.get(i);
			if(c2!=c){
				System.err.println("cache no "+i+" ,"+c2.lineSize+", assoc is "+c2.getAssoc());

			if(c2.getAssoc()==1){//direct map
				System.out.println("i am in a direct map");
			int index2=physicaladdress%c2.getNoOFBlocks();
			int tag=physicaladdress/c2.getNoOFBlocks();
			System.err.println("iam in direct map tag is "+tag+" index is "+ index2);
			if(c2.getWritePolicy()==0){//write through
				if(c2.getContent()[index2][c2.getContent()[index2].length-2]!=null&&
						c2.getContent()[index2][c2.getContent()[index2].length-2].equals(String.valueOf(tag))){
					System.out.println("direct map and foun it ");
					for (int j = 0; j < c2.getContent()[index2].length-2; j++) {//write the new block byte by byte
						c2.getContent()[index2][j]=tmp[j];
						System.out.println("copying in direct map"+tmp[j]);
					}
					
				}
				
			}
			else{//write back
					if(c2.getContent()[index2][c2.getContent()[index2].length-3]!=null&&c2.getContent()[index2][c2.getContent()[index2].length-3].equals(String.valueOf(tag))){
						for (int j = 0; j < c2.getContent()[index].length-3; j++) {
							c2.getContent()[index2][j]=tmp[j];
						}
						
					}
				
				
			
			}
			}
			
			else if(c2.getAssoc()==c2.getNoOFBlocks()){//full associative
				System.err.println("i am full associative ");
				int index3=-1 ;
				int tag=physicaladdress;
				if(c2.getWritePolicy()==0){
					
					for (int j = 0; j < c2.getContent().length; j++) {//search in all blocks
						if(c2.getContent()[j][c2.getContent()[j].length-2]!=null&&c2.getContent()[j][c2.getContent()[j].length-2].equals(String.valueOf(tag))){//search for the block in the whole cache 
							index3=j;
							System.err.println("found it !");
									break;
						}
						
					}
					if(index3!=-1){
						
					for (int j = 0; j < c2.getContent()[index3].length-2; j++) {
							c2.getContent()[index3][j]=tmp[j];
							System.err.println("copying .."+ tmp[j]+"value of i is "+ j);
						}
						
					}
				}
				
				else {//write back
					for (int j = 0; j < c2.getContent().length; j++) {
						if(c2.getContent()[j][c2.getContent()[j].length-3]!=null
								&&c2.getContent()[j][c2.getContent()[j].length-3].equals(String.valueOf(tag))){//search for the block in the whole cache 
							index3=j;
							System.out.println("direct map write back and found it ");
									break;
						}
						
					}
					if(index3!=-1){
					for (int j = 0; j < c2.getContent()[index3].length-3; j++) {
						c2.getContent()[index3][j]=tmp[j];
						System.out.println("copying direct map write back"+j+" value "+tmp[j]);
					}
			
					}
			}
			
			
		}
			else{//set associative 
				System.out.println("iam set **");
				int no_of_sets=c2.getNoOFBlocks()/c2.getAssoc();
				int set_no=physicaladdress%no_of_sets;
				int tag=physicaladdress/no_of_sets;
				int startBlock=set_no*(c2.getNoOFBlocks()/c2.getAssoc());
				int index3=-1;
				System.out.println("tag is "+tag +" start block is  "+startBlock);
			
				////////////////
				if(c2.getWritePolicy()==0){

					for (int j =0; j < c2.getAssoc(); j++) {
						if(c2.getContent()[startBlock][c2.getContent()[j].length-2]!=null
						 &&c2.getContent()[startBlock][c2.getContent()[0].length-2].equals(String.valueOf(tag))){
							index3=startBlock;
							System.out.println("iam set and i found it ");
							break;
							
						}
						++startBlock;
					}
					if(index3!=-1){
						System.err.println(" i found it copy in "+ startBlock);
					
					for (int j = 0; j < c2.getContent()[startBlock].length-2; j++) {
						c2.getContent()[startBlock][j]=tmp[j];
						System.out.println("coping in the set "+tmp[j]+" ,j is "+ j);
						
					}
					}
				}
				else{

					for (int j =0; j < c2.getAssoc(); j++) {
						if(c2.getContent()[j][c2.getContent()[j].length-3]!=null&&c2.getContent()[startBlock][c2.getContent()[0].length-3].equals(String.valueOf(tag))){
							index3=startBlock;break;
						}
						++startBlock;
					}
					if(index3!=-1){
					for (int j = 0; j < c2.getContent()[startBlock].length-3; j++) {
						c2.getContent()[startBlock][j]=tmp[j];
						
					}
					}
					
				}
				///////////////////////
				
				
			}
		}
		}
		
	}
	/////////////////////////
	
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
