

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

import javax.swing.JFrame;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// this class handles the processor functions and contains all the processor registers

public class Processor {
	
	private static JFrame frame;
	
//	Initialize the registers and the cache levels
//	cache levels will end with the memory level but to be handled after user input
	
	private static int[] register = new int[32];
	static ArrayList<String>lines = new ArrayList<String>();
	private static ArrayList <Cache> cacheLevel = new ArrayList<Cache>();
	private static ArrayList <Cache> iCache;
	static Hashtable<String, String> labels = new Hashtable<String,String>();
	static int PC;
	static int cycles = 0;
	private static JTextField addi_number;
	private static JTextField addd_number;
	private static JTextField cache_levels_field;
	private static JTextField line_size_field;
	private static JTextField n_ways_field;
	private static JTextField write_policy_field;
	private static JTextField number_of_cycles_field;
	private static JTextField memory_access_time_field;
	private static JTextField instruction_queue_field;
	private static JTextField rob_entries_field;
	private static JTextField multd_number;
	private static JTextField load_number;
	private static JTextField store_number;
	private static JTextField addi_cycles;
	private static JTextField addd_cycles;
	private static JTextField multd_cycles;
	private static JTextField load_cycles;
	private static JTextField store_cycles;
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

	public void init() 
	{// method init takes input file from the user and
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
	
	///// omar's work starts here
	
	public static Instruction[] fetch()
	{
		// WARNING
		// i initialized a temporary instruction class at the bottom of this file
		
		
		
		// instruction(name, type, rd, rs, rt)
		Instruction[] fetched = new Instruction[4];
		
		for (int i = 0; i < fetched.length; i++) 
		{
			//	fetch every instruction from lines from pc to pc + 3
			// create an object of that instruction
			// add it to the array
			String tempLine = lines.get(PC+ i);
			if (tempLine == null) 
			{
				System.out.println("Reached end of input");
				return fetched;
			}
			
			String [] sLine;
			if (tempLine.contains(":")) 
			{
				sLine = tempLine.split(":")[1].split(" ");
			}
			else
			{
				sLine = tempLine.split(" ");
			}
			String [] regs = sLine[1].split(",");
			
//			not really sure of how correct is my call to the hitOrMiss method
			for (int j = 0; j < iCache.size(); j++) 
			{
				if (iCache.get(j).hitOrMissDM(PC+1)) 
				{
					// this should add to fetched[i] the content of the iCache at j which is supposed to be an instruction but its not
					// the cache is read and parsed into a new instruction which is put in the fetched array
					String [] read = iCache.get(j).readDM(PC);
					fetched[i] = new Instruction(read[0], read[1], read[2], read[3], read[4]);
					cycles += 1;
				}
				else
				{
					
					cycles += iCache.get(j).getCycles();
					switch (sLine[0].toLowerCase()) 
					{
					case "add": fetched[i] = new Instruction("Add", "Add", regs[0], regs[1], regs[2]); break;
					case "sub": fetched[i] = new Instruction("Sub", "Add", regs[0], regs[1], regs[2]); break;
					case "beq":
						// if the content of regs[2] is present in the labels get its PC value from the hashtable
						// else put the number directly
						
						// NOTE THAT
						// the "beq" instruction should depend on the offset and it is not handled...yet
						if (labels.containsKey(regs[2])) 
						{
							fetched[i] = new Instruction("beq", "Add", regs[0], regs[1], labels.get(regs[2]));
						}
						else
						{
							fetched[i] = new Instruction("beq", "Add", regs[0], regs[1], ((PC + 1 + Integer.parseInt(regs[2]))+""));
						} 
						break;
					case "load": fetched[i] = new Instruction("load", "load", regs[0], regs[1], regs[2]); break;
					case "store": fetched[i] = new Instruction("store", "load", regs[0], regs[1], regs[2]); break;
					case "mult": fetched[i] = new Instruction("mult", "mult", regs[0], regs[1], regs[2]); break;
					case "div": fetched[i] = new Instruction("div", "mult", regs[0], regs[1], regs[2]); break;
					case "jalr":
						int saveTo = Integer.parseInt(regs[0].toLowerCase().split("r")[1]);
						if (saveTo > 31 || saveTo < 1) 
						{
							System.out.println("Sth is wrong with the register to save to in jalr in fetch method");
						}
						else
						{
							register[saveTo] = PC+1;
							PC = Integer.parseInt(regs[1]);
						}
						fetched[i] = new Instruction("jarl", "add", regs[0], regs[1], regs[2]);
						break;
						// keep in mind here it assumes that the registers will be from 0 to 31
						// if out of bounds it will give a null pointer exception which indicates compiling error for user
					case "ret":
						PC = register[Integer.parseInt(regs[0])];
						fetched[i] = new Instruction("jarl", "add", regs[0], null, null);
						break;
					case "jmp":
						PC = register[Integer.parseInt(regs[0])];
						fetched[i] = new Instruction("jarl", "add", regs[0], regs[1], null);
						break;
					case "nand": fetched[i] = new Instruction("nand", "add", regs[0], regs[1], regs[2]); break;
					case "addi": fetched[i] = new Instruction("addi", "add", regs[0], regs[1], regs[2]); break;

					default: System.out.println("Something is wrong in fetch() switch statement");break;
					}
					// writes the fetched instruction to the iCache					
					iCache.get(j).writeDM(PC, fetched[i].toString().substring(1, fetched[i].toString().length()-1).split(","));
					// always increment the PC after each fetch
					PC++;
				}
			}
		}
		
		return fetched;
		
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
			//System.out.println("index is "+ index);
			//System.out.println(Arrays.toString(c.getContentOf(BlockNo)));
			if(c.getWritePolicy()==0){ //write through
				
				tmp=Integer.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c.getContent()[BlockNo].length-2]))+index;
						//Integer.toBinaryString(c.offsetBits);
				//tmp2=(Integer.parseInt(c.getContent()[BlockNo][c.getContent().length-2])/(1-(1/c.getNoOFBlocks())*c.getNoOFBlocks()));
				}
				else{ //write back
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

	public static ArrayList<Cache> getCacheLevel() {
		return cacheLevel;
	}

	public static void setCacheLevel(ArrayList<Cache> cacheLevel) {
		Processor.cacheLevel = cacheLevel;
	}

	public static String[] cacheAccesRead(int address) {
         return null;
		// TO-DO implement read method for memory hierarchy
	}
	public static void cacheAccessWrite(int address, String[] data) {
		//implement write to memory level
	}

	/////////////////////////
	public static void main(String[] args) 
	{
		initialize();
		/* GUI STUFF */
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		/* the rest */
		
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
		iCache = new ArrayList<Cache>(cacheLevel);		
		Processor p = new Processor();
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("add $t0,$t1,$t4");
        lines.add("loop: add $t0,$t1,$t4");
		lines.add("zeft: add $t0,$t1,$t4");
		System.out.println(p.compile(lines));
		Cache c1 = new Cache(32, 4, 1, 0, 1);
		Cache c2 = new Cache(64, 4, 1, 0, 4);
		cacheLevel.add(c1);
		cacheLevel.add(c2);

	}

	/**
	 * Launch the application.
	 */

//	/**
//	 * Create the application.
//	 */
//	public GUI() {
//		initialize();
//	}

	/**
	 * Initialize the contents of the frame.
	 */
	private static void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 733, 482);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblAddi = new JLabel("ADD.I");
		lblAddi.setBounds(264, 42, 38, 16);
		frame.getContentPane().add(lblAddi);
		
		JLabel lblAddd = new JLabel("ADD.D");
		lblAddd.setBounds(264, 71, 38, 16);
		frame.getContentPane().add(lblAddd);
		
		addi_number = new JTextField();
		addi_number.setBounds(347, 39, 56, 22);
		frame.getContentPane().add(addi_number);
		addi_number.setColumns(10);
		
		addd_number = new JTextField();
		addd_number.setBounds(347, 68, 56, 22);
		frame.getContentPane().add(addd_number);
		addd_number.setColumns(10);
		
		JLabel lblCacheLevels = new JLabel("Cache Levels");
		lblCacheLevels.setBounds(12, 13, 79, 16);
		frame.getContentPane().add(lblCacheLevels);
		
		cache_levels_field = new JTextField();
		cache_levels_field.setBounds(139, 10, 56, 22);
		frame.getContentPane().add(cache_levels_field);
		cache_levels_field.setColumns(10);
		
		JLabel lblLineSize = new JLabel("Line Size");
		lblLineSize.setBounds(12, 42, 56, 16);
		frame.getContentPane().add(lblLineSize);
		
		JLabel lblNways = new JLabel("n-ways");
		lblNways.setBounds(12, 71, 56, 16);
		frame.getContentPane().add(lblNways);
		
		line_size_field = new JTextField();
		line_size_field.setBounds(139, 39, 56, 22);
		frame.getContentPane().add(line_size_field);
		line_size_field.setColumns(10);
		
		n_ways_field = new JTextField();
		n_ways_field.setBounds(139, 68, 56, 22);
		frame.getContentPane().add(n_ways_field);
		n_ways_field.setColumns(10);
		
		JLabel lblWritePolicy = new JLabel("Write policy");
		lblWritePolicy.setBounds(12, 106, 79, 16);
		frame.getContentPane().add(lblWritePolicy);
		
		write_policy_field = new JTextField();
		write_policy_field.setBounds(139, 100, 56, 22);
		frame.getContentPane().add(write_policy_field);
		write_policy_field.setColumns(10);
		
		JLabel lblCycles = new JLabel("No. of cycles");
		lblCycles.setBounds(12, 135, 79, 16);
		frame.getContentPane().add(lblCycles);
		
		number_of_cycles_field = new JTextField();
		number_of_cycles_field.setBounds(139, 132, 56, 22);
		frame.getContentPane().add(number_of_cycles_field);
		number_of_cycles_field.setColumns(10);
		
		JLabel lblMemAccessTime = new JLabel("Mem. access time");
		lblMemAccessTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMemAccessTime.setBounds(12, 164, 91, 22);
		frame.getContentPane().add(lblMemAccessTime);
		
		memory_access_time_field = new JTextField();
		memory_access_time_field.setBounds(139, 163, 56, 22);
		frame.getContentPane().add(memory_access_time_field);
		memory_access_time_field.setColumns(10);
		
		JLabel lblInstructionQueue = new JLabel("Instruction Queue");
		lblInstructionQueue.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInstructionQueue.setBounds(12, 199, 91, 16);
		frame.getContentPane().add(lblInstructionQueue);
		
		instruction_queue_field = new JTextField();
		instruction_queue_field.setBounds(139, 195, 56, 22);
		frame.getContentPane().add(instruction_queue_field);
		instruction_queue_field.setColumns(10);
		
		JLabel lblRobEntries = new JLabel("ROB entries");
		lblRobEntries.setBounds(264, 198, 79, 16);
		frame.getContentPane().add(lblRobEntries);
		
		rob_entries_field = new JTextField();
		rob_entries_field.setBounds(347, 195, 56, 22);
		frame.getContentPane().add(rob_entries_field);
		rob_entries_field.setColumns(10);
		
		JLabel lblLoad = new JLabel("LOAD");
		lblLoad.setBounds(264, 135, 56, 16);
		frame.getContentPane().add(lblLoad);
		
		JLabel lblStore = new JLabel("STORE");
		lblStore.setBounds(264, 166, 56, 16);
		frame.getContentPane().add(lblStore);
		
		JLabel lblMultd = new JLabel("MULT.D");
		lblMultd.setBounds(264, 106, 56, 16);
		frame.getContentPane().add(lblMultd);
		
		multd_number = new JTextField();
		multd_number.setBounds(347, 103, 56, 22);
		frame.getContentPane().add(multd_number);
		multd_number.setColumns(10);
		
		load_number = new JTextField();
		load_number.setBounds(347, 132, 56, 22);
		frame.getContentPane().add(load_number);
		load_number.setColumns(10);
		
		store_number = new JTextField();
		store_number.setBounds(347, 163, 56, 22);
		frame.getContentPane().add(store_number);
		store_number.setColumns(10);
		
		JLabel lblUnit = new JLabel("Unit");
		lblUnit.setBounds(264, 13, 56, 16);
		frame.getContentPane().add(lblUnit);
		
		JLabel lblNumber = new JLabel("Number");
		lblNumber.setBounds(347, 13, 56, 16);
		frame.getContentPane().add(lblNumber);
		
		JLabel lblCycles_1 = new JLabel("Cycles");
		lblCycles_1.setBounds(438, 13, 56, 16);
		frame.getContentPane().add(lblCycles_1);
		
		addi_cycles = new JTextField();
		addi_cycles.setBounds(438, 39, 64, 22);
		frame.getContentPane().add(addi_cycles);
		addi_cycles.setColumns(10);
		
		addd_cycles = new JTextField();
		addd_cycles.setBounds(438, 68, 64, 22);
		frame.getContentPane().add(addd_cycles);
		addd_cycles.setColumns(10);
		
		multd_cycles = new JTextField();
		multd_cycles.setBounds(438, 103, 64, 22);
		frame.getContentPane().add(multd_cycles);
		multd_cycles.setColumns(10);
		
		load_cycles = new JTextField();
		load_cycles.setBounds(438, 132, 64, 22);
		frame.getContentPane().add(load_cycles);
		load_cycles.setColumns(10);
		
		store_cycles = new JTextField();
		store_cycles.setBounds(438, 163, 64, 22);
		frame.getContentPane().add(store_cycles);
		store_cycles.setColumns(10);
		
		JTextArea code_area = new JTextArea();
		code_area.setBounds(22, 255, 280, 167);
		frame.getContentPane().add(code_area);
		
		JTextArea output_area = new JTextArea();
		output_area.setBounds(347, 255, 356, 167);
		frame.getContentPane().add(output_area);
		
		JLabel lblYourCodeGoes = new JLabel("Your code goes here");
		lblYourCodeGoes.setBounds(90, 228, 118, 16);
		frame.getContentPane().add(lblYourCodeGoes);
		
		JLabel lblOutput = new JLabel("OUTPUT");
		lblOutput.setBounds(499, 228, 56, 16);
		frame.getContentPane().add(lblOutput);
		
		JButton btnRunCode = new JButton("Run Code");
		btnRunCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				// compile the code by taking all arguments from the fields
				
				// take the input and put it in a text file
				File input = new File("input.txt", code_area.getText());
			}
		});
		btnRunCode.setBounds(578, 162, 97, 25);
		frame.getContentPane().add(btnRunCode);
	}
}

//temporary instruction class... remove when the original is done
//class Instruction
//{
//	String name;
//	String type;
//	String rs;
//	String rd;
//	String rt;
//	
//	public Instruction(String name, String type, String rs, String rd, String rt)
//	{
//		// testing only			
//	};
//
//}
