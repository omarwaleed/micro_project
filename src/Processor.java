

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// this class handles the processor functions and contains all the processor registers

public class Processor {
	
//	Initialize the registers and the cache levels
//	cache levels will end with the memory level but to be handled after user input
	
	private static int[] register = new int[32];
	ArrayList<String>lines = new ArrayList<String>();
	private static ArrayList <Cache> cacheLevel = new ArrayList<Cache>();
	Hashtable<String, String> labels = new Hashtable<String,String>();
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

	public void init() {// method init takes input file from the user and
 	// compiles the file and handle pseudo
 	// instructions
 try {

 ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(
 Paths.get("src/input.txt"), StandardCharsets.UTF_8);
 getLabels(lines);
 if (validateLabels(lines) && compile(lines)) {
    this.lines = lines;
 } else {
 System.out.println("your code contains errors!");
 }
 } catch (IOException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
}
	public boolean isIFormat(String instruction) {
		return (instruction
				.matches("^\\w*\\s*\\:?\\s*(addi|lui)\\s*(\\$\\w\\d?\\,\\s*){2}\\s*\\d*$")
				|| instruction
				.matches("^\\w*\\s*\\:?\\s*(lw|sw|lb|lbu|sb)(\\s*)\\$(\\w{1,2}|\\w{4})(\\d?)(\\,)(\\s+)(\\d+)\\(\\$(\\w{1,2}|\\w{4})\\)\\s*$") || instruction
				.matches("^\\w*\\s*\\:?\\s*(bne|beq)\\s*(\\$\\w\\d?\\,\\s*){2}\\s*\\w*$"));
	}

	public boolean isJFormat(String instruction) {
		return instruction.matches("^\\w+\\s*\\:?\\s*\\w*\\s+\\w*\\s*$");
	}

	public boolean isSupported(String instName) {
		return (instName.equalsIgnoreCase("add")
				|| instName.equalsIgnoreCase("sub")
				|| instName.equalsIgnoreCase("lw")
				|| instName.equalsIgnoreCase("addi")
				|| instName.equalsIgnoreCase("sw")
				|| instName.equalsIgnoreCase("lb")
				|| instName.equalsIgnoreCase("sb")
				|| instName.equalsIgnoreCase("lbu")
				|| instName.equalsIgnoreCase("lui")
				|| instName.equalsIgnoreCase("slt")
				|| instName.equalsIgnoreCase("sltu")
				|| instName.equalsIgnoreCase("and")
				|| instName.equalsIgnoreCase("nor")
				|| instName.equalsIgnoreCase("sll")
				|| instName.equalsIgnoreCase("srl")
				|| instName.equalsIgnoreCase("beq")
				|| instName.equalsIgnoreCase("bne")
				|| instName.equalsIgnoreCase("j")
				|| instName.equalsIgnoreCase("jal")
				|| instName.equalsIgnoreCase("jr")
				|| instName.equalsIgnoreCase("move")
				|| instName.equalsIgnoreCase("blt") || instName
				.equalsIgnoreCase("la"));
	}

	public boolean isJump(String instName) {
		return (instName.equalsIgnoreCase("j")
				|| instName.equalsIgnoreCase("jal") || instName
				.equalsIgnoreCase("jr"));
	}
	public boolean isRFormat(String instruction) {
		return (instruction
				.matches("^(\\w*\\s*\\:?)\\s*(add|sub|slt|sltu|and|nor)\\s+\\$\\w\\d,\\s*\\$\\w\\d,\\s*\\$\\w\\d\\s*$")
				|| instruction
				.matches("^(\\w*\\s*\\:?)\\s*(sll|srl)\\s+\\$\\w*\\d?,\\s*\\$\\w*\\d?,\\s*\\d*$") || instruction
				.matches("^(\\w*\\s*\\:?)\\s*(jr)\\s+\\$\\w{2}$"));
	}
	public void getLabels(ArrayList<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).contains(":")
					&& (isRFormat(lines.get(i)) || isJFormat(lines.get(i))
					|| isIFormat(lines.get(i)))) {
				String[] label = lines.get(i).split(":");
				labels.put(label[0], i + "");
			}
		}
	}

	public boolean validateLabels(ArrayList<String> lines) {
		boolean labelEB = true;
		boolean labelEJ = true;
		boolean labelJR = true;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).contains("bne") || lines.get(i).contains("beq")) {
				String label = lines.get(i).split(",")[2];
				// System.out.println("hello");
				for (int j = 0; j < labels.size(); j++) {
					if (!labels.containsKey(label)) {
						labelEB = false;
						break;
					}
				}
			}
			if ((lines.get(i).contains("j") || lines.get(i).contains("jal"))
					&& isJFormat(lines.get(i))) {
				String label = lines.get(i).split(" ")[1];
				System.out.println("label" + label);
				for (int j = 0; j < labels.size(); j++) {
					if (!labels.containsKey(label)) {
						labelEJ = false;
						break;
					}
				}
			}
			if (lines.get(i).contains("jr")) {
				String[] label = lines.get(i).split(" ");
				//System.out.println(label.length);
				//System.out.println(label[0] + " " + label[1]);
				if (label.length < 2) {
					labelJR = false;
					//System.out.println("here also");
					break;
				}
				// label[1] = label[1].trim();
				if (!((label[1].equals("$ra") || label[1].equals("$t1")
						|| label[1].equals("$t0") || label[1].equals("$t2")
						|| label[1].equals("$t3") || label[1].equals("$t4")
						|| label[1].equals("$t5") || label[1].equals("$t6")
						|| label[1].equals("$t7") || label[1].equals("$t8")
						|| label[1].equals("$t9") || label[1].equals("$s0")
						|| label[1].equals("$s1") || label[1].equals("$s2")
						|| label[1].equals("$s3") || label[1].equals("$s4")
						|| label[1].equals("$s5") || label[1].equals("$s6")
						|| label[1].equals("$s7") || label[1].equals("$a0")
						|| label[1].equals("$a1") || label[1].equals("$a2")
						|| label[1].equals("$v1") || label[1].equals("$v0")))) {
					labelJR = false;
					//System.out.println("here");
					break;
				}
			}

		}
		return labelEB && labelEJ && labelJR;
	}

	public boolean compile(ArrayList<String> lines) {
		boolean compiled = true;
		for (int i = 0; i < lines.size(); i++) {
			if ((!isRFormat(lines.get(i)) && !isIFormat(lines.get(i))
					&& !isJFormat(lines.get(i)))
					&& !isSupported(lines.get(i).split(" ")[0].toLowerCase()
					.trim()) && !lines.get(i).equals("halt")) {
				System.out.println("in bad condition " + lines.get(i));
				System.out.println("!R format:" + !isRFormat(lines.get(i)));
				System.out.println("!J format:" + !isJFormat(lines.get(i)));
				System.out.println("!I format:" + !isIFormat(lines.get(i)));
				System.out.println("!supported:"
						+ !isSupported(lines.get(i).split(" ")[0].toLowerCase()
						.trim()));
				compiled = false;
				break;
			}
		}
		return compiled;
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
				System.err.println("cache no "+i+" ,"+c2.getLineSize() +", assoc is "+c2.getAssoc());

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
		
		Processor p = new Processor();
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("add $t0,$t1,$t4");
        lines.add("loop: add $t0,$t1,$t4");
		lines.add("zeft: add $t0,$t1,$t4");
		System.out.println(p.compile(lines));

	}
	 

}
