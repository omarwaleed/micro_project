import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

// this class handles the processor functions and contains all the processor registers

public class Processor {

	// Initialize the registers and the cache levels
	// cache levels will end with the memory level but to be handled after user
	// input
	// private static int[] register = new int[32];
	public static Hashtable<String, Integer> registers = new Hashtable<String, Integer>();// modified
	static Scoreboard scoreBoard;
	static int PC = 0;
	static int cycles = 1;
	private static ArrayList<Cache> cacheLevel = new ArrayList<Cache>();
	static ArrayList<Instruction> issue = new ArrayList<Instruction>();
	static ArrayList<FU> calculateAdress = new ArrayList<FU>();
	static ArrayList<FU> Excute = new ArrayList<FU>();
	static ArrayList<FU> Writebs = new ArrayList<FU>();
	static ArrayList<FU> commits = new ArrayList<FU>();

	public Processor() {
		scoreBoard = new Scoreboard(this);
		
	}

	// ////////////////////////tomosolo
	public static void Issue(int cycle) {
		// int size=issue.size();
		ArrayList<Instruction>tmp= new ArrayList<Instruction>();
		for (int i = 0; i < issue.size(); i++) {
			Instruction x = issue.get(i);
			System.out.println("instruction name   " + x.name);

			if (!scoreBoard.fullFuncUnit(x.type) && !scoreBoard.robFull()) {
				System.err.println("i found a place ");
				if (x.name.contains("load")) {

					FU y = scoreBoard.getFU(x.type);
					y.busy = true;
					y.op = x.name;
					y.vj = x.rs;// has only one resource
					Entry entry = new Entry(x.type, x.rd);// to insert it n the
															// ROB
					entry.occupied = true;
					scoreBoard.insertROB(entry);
					y.dest = scoreBoard.tail - 1;
					y.a = x.offset;// set the offset
					if (scoreBoard.registerStatus.containsKey(y.vj)) {
						y.qj = String.valueOf(scoreBoard.registerStatus
								.get(y.vj));

					}

					scoreBoard.registerStatus.put(x.rd, scoreBoard.tail - 1);//
					scoreBoard.instructions.put(x, "issued :" + cycles);
					x.lastCycle = cycles;
					calculateAdress.add(y);
					tmp.add(x);
					y.i = x;
					// issue.remove(x);

				} else if (x.name.contains("store")) {
					Entry entry = new Entry(x.type, "memory");// it has no rd so
																// we write
																// memory
					entry.occupied = true;
					scoreBoard.insertROB(entry);// he wont write in the register
												// status
					FU y = scoreBoard.getFU(x.type);
					y.busy = true;
					y.op = x.name;
					y.vj = x.rs;
					y.vk = x.rt;// it has to resources one to calculate the
								// address
					y.dest = scoreBoard.tail - 1; // and the other to store its
													// value
					y.a = x.offset;// set the offset
					if (scoreBoard.registerStatus.containsKey(y.vj)) {
						y.qj = String.valueOf(scoreBoard.registerStatus
								.get(y.vj));

					}
					if (scoreBoard.registerStatus.containsKey(y.vk)) {
						y.qk = String.valueOf(scoreBoard.registerStatus
								.get(y.vk));

					}
					scoreBoard.instructions.put(x, "issued :" + cycles);
					x.lastCycle = cycles;
					calculateAdress.add(y);
					tmp.add(x);
					// issue.remove(x);
					y.i = x;
				} else if (x.name.contains("addi")) {// add immediate
					Entry entry = new Entry(x.type, x.rd);// to insert it n the
															// ROB
					scoreBoard.insertROB(entry);
					entry.occupied = true;
					FU y = scoreBoard.getFU(x.type);
					y.busy = true;
					y.op = x.name;
					y.vj = x.rs;// has only one resource
					y.dest = scoreBoard.tail - 1;
					y.a = x.offset;// set the offset
					if (scoreBoard.registerStatus.containsKey(y.vj)) {
						y.qj = String.valueOf(scoreBoard.registerStatus
								.get(y.vj));

					}
					scoreBoard.registerStatus.put(x.rd, scoreBoard.tail - 1);//
					scoreBoard.instructions.put(x, "issued :" + cycles);
					x.lastCycle = cycles;
					Excute.add(y);
					tmp.add(x);
					// issue.remove(x);
					y.i = x;

				} else if (x.name.contains("beq")) {
					Entry entry = new Entry(x.type, x.rd);// to insert it n the
															// ROB
					entry.occupied = true;
					scoreBoard.insertROB(entry);

					FU y = scoreBoard.getFU(x.type);
					y.busy = true;
					y.op = x.name;
					y.vj = x.rs;
					y.vk = x.rt;
					y.dest = scoreBoard.tail - 1;
					// setting QJ and QK
					if (scoreBoard.registerStatus.containsKey(y.vj)) {
						y.qj = String.valueOf(scoreBoard.registerStatus
								.get(y.vj));

					}
					if (scoreBoard.registerStatus.containsKey(y.vk)) {
						y.qk = String.valueOf(scoreBoard.registerStatus
								.get(y.vk));

					}
					scoreBoard.instructions.put(x, "issued :" + cycles);
					// System.out.println("before excution");
					x.lastCycle = cycles;
					// scoreBoard.print_scoreboard();
					Excute.add(y);
					tmp.add(x);
					// issue.remove(x);
					y.i = x;

				} else {// for add Malt etc..
					Entry entry = new Entry(x.type, x.rd);// to insert it n the
															// ROB
					entry.occupied = true;
					scoreBoard.insertROB(entry);

					FU y = scoreBoard.getFU(x.type);
					y.busy = true;
					y.op = x.name;
					y.vj = x.rs;
					y.vk = x.rt;
					y.dest = scoreBoard.tail - 1;
					// setting QJ and QK
					if (scoreBoard.registerStatus.containsKey(y.vj)) {
						y.qj = String.valueOf(scoreBoard.registerStatus
								.get(y.vj));

					}
					if (scoreBoard.registerStatus.containsKey(y.vk)) {
						y.qk = String.valueOf(scoreBoard.registerStatus
								.get(y.vk));

					}
					scoreBoard.registerStatus.put(x.rd, scoreBoard.tail - 1);//
					scoreBoard.instructions.put(x, "issued :" + cycles);
					x.lastCycle = cycles;
					// System.out.println("before excution");
					// scoreBoard.print_scoreboard();
					Excute.add(y);
					tmp.add(x);
					// issue.remove(x);
					y.i = x;
					 
					 //scoreBoard.tail = scoreBoard.tail++;
					System.out.println("i will increment the tail " +scoreBoard.tail);

				}

			} else {
				System.err.println("no place for u here");
				System.out.println("after issueing");
				System.out.println(scoreBoard.toString());
				// if one instruction did not find a place
				// ++cycles;
				for (int j = 0; j < tmp.size(); j++) {
					issue.remove(tmp.get(j));
					}
				tmp.clear();
				if(calculateAdress.size()>0)
					calculate_address(cycle + 1);// all instruction after him should
												// be stuck as the issue is in
				if(Excute.size()>0)								// order
					Excute(cycle + 1);
				return;

			}
			// list.remove(x);
		}
		++cycles;
		System.out.println("after issueing");
		System.out.println(scoreBoard.toString());
		for (int j = 0; j < tmp.size(); j++) {
			issue.remove(tmp.get(j));
			}
		tmp.clear();
		if(calculateAdress.size()>0)
			calculate_address(cycle + 1);
		
		System.out.println("Excute size is " + Excute.size());
		if(Excute.size()>0)	
			Excute(cycle + 1);

	}

	//////////////////////////////////////////////////////////////////////////////// /end of issue
	public static void calculate_address(int cycle) {
		ArrayList <FU>tmp=new ArrayList<FU>();
		System.out.println("iam calculating add");
		for (int i = 0; i < calculateAdress.size(); i++) {
			FU x = calculateAdress.get(i);
			if (x.type.contains("load")) {

				if (x.qj == null) {// it does not wait any one
					System.out.println("vj is equal to " + x.vj);
					int add = Integer.parseInt(x.a) + registers.get(x.vj);
					x.a = String.valueOf(add);
					scoreBoard.instructions.put(x.i,
							scoreBoard.instructions.get(x.i) + ", CalcAdd :"
									+ cycle);
					x.i.lastCycle = cycle + 1;
					Excute.add(x);
					tmp.add(x);
					// calculateAdress.remove(x);
				} else {// it depends on another instruction
					int entry_no = Integer.parseInt(x.qj);
					Entry entry = scoreBoard.rob[entry_no];
					if (entry.ready) {
						int add = Integer.parseInt(x.a)
								+ Integer.parseInt(entry.value);
						x.a = String.valueOf(add);
						Excute.add(x);
						tmp.add(x);
						scoreBoard.instructions.put(x.i,
								scoreBoard.instructions.get(x.i)
										+ ", CalcAdd :" + cycle);
						x.i.lastCycle = cycle + 1;
						// calculateAdress.remove(x);

					}
				}

			} else {// if instruction is store
				if (x.qk == null) {// it does not wait any one
					int add = Integer.parseInt(x.a) + registers.get(x.vk);
					x.a = String.valueOf(add);
					Excute.add(x);
					tmp.add(x);
					scoreBoard.instructions.put(x.i,
							scoreBoard.instructions.get(x.i) + ", CalcAdd :"
									+ cycle);
					x.i.lastCycle = cycle + 1;
					// calculateAdress.remove(x);
				} else {// it depends on another instruction
					int entry_no = Integer.parseInt(x.qk);
					Entry entry = scoreBoard.rob[entry_no];
					if (entry.ready) {
						int add = Integer.parseInt(x.a)
								+ Integer.parseInt(entry.value);
						x.a = String.valueOf(add);
						Excute.add(x);
						tmp.add(x);
						scoreBoard.instructions.put(x.i,
								scoreBoard.instructions.get(x.i)
										+ ", CalcAdd :" + cycle);
						x.i.lastCycle = cycle + 1;
						calculateAdress.remove(x);
						//
					}
				}

			}

		}
		///////////////////////////////////////end of calculate address 
		// Excute(cycle+1);
		for (int i = 0; i < tmp.size(); i++) {
			calculateAdress.remove(tmp.get(i));
		}
		tmp.clear();

	}

	public static void Excute(int cycle) {
		ArrayList <FU>tmp=new ArrayList<FU>();
		System.err.println("excute size  "+Excute.size());
		for (int i = 0; i < Excute.size(); i++) {
			FU y = (FU) Excute.get(i);
			if (y.busy) {
				if (y.type.contains("load")) {
					y.execute();
					if (y.i.lastCycle > cycle)
						y.i.lastCycle = y.i.lastCycle + y.latency - 1;
					else
						y.i.lastCycle = cycle + y.latency - 1;
					scoreBoard.instructions.put(y.i,
							scoreBoard.instructions.get(y.i) + ",Excuted :"
									+ y.i.lastCycle);
						Writebs.add(y);
						tmp.add(y);
					// Excute.remove(i);
				} else if (y.type.contains("store")) {
					if (y.qk == null) {
						y.execute();

						if (y.i.lastCycle > cycle)
							y.i.lastCycle += y.latency - 1;// should be modified
															// and el cache
						else
							y.i.lastCycle = cycle + y.latency - 1;

						scoreBoard.instructions.put(y.i,
								scoreBoard.instructions.get(y.i) + ",Excuted :"
										+ y.i.lastCycle);
						Writebs.add(y);
						tmp.add(y);
						// Excute.remove(i);
					} else {// waiting for another one
						int entry_no = Integer.parseInt(y.qk);
						Entry entry = scoreBoard.rob[entry_no];
						if (entry.ready) {
							y.execute();
							y.i.lastCycle = cycle + y.latency - 1;
							scoreBoard.instructions.put(y.i,
									scoreBoard.instructions.get(y.i)
											+ ",Excuted :" + y.i.lastCycle);
							Writebs.add(y);
							tmp.add(y);
							// Excute.remove(i);

						}

					}

				} else if (y.op.contains("addi")) {// addi
					if (y.qj == null) {
						y.execute();
						y.i.lastCycle = cycle + y.latency - 1;
						scoreBoard.instructions.put(y.i,
								scoreBoard.instructions.get(y.i) + ",Excuted :"
										+ y.i.lastCycle);
						Writebs.add(y);
						tmp.add(y);

						// Excute.remove(i);
					} else {
						int entry_no = Integer.parseInt(y.qj);
						Entry entry = scoreBoard.rob[entry_no];
						if (entry.ready) {
							y.execute();
							y.i.lastCycle = cycle + y.latency - 1;
							scoreBoard.instructions.put(y.i,
									scoreBoard.instructions.get(y.i)
											+ ",Excuted :" + y.i.lastCycle);
							Writebs.add(y);
							tmp.add(y);

						}

					}

				}

				else {// MAlt || add etc ..||beq
					if ((y.qj == null && y.qk == null)) {
						y.execute();
						System.out.println("last cycle is " + y.i.lastCycle);
						y.i.lastCycle = cycle + y.latency - 1;
						scoreBoard.instructions.put(y.i,
								scoreBoard.instructions.get(y.i) + ",Excuted :"
										+ y.i.lastCycle);
						Writebs.add(y);
						tmp.add(y);

						// Excute.remove(i);
					} else if (y.qj != null && y.qk != null) {

						int entry_no1 = Integer.parseInt(y.qj);
						int entry_no2 = Integer.parseInt(y.qk);
						Entry entry1 = scoreBoard.rob[entry_no1];
						Entry entry2 = scoreBoard.rob[entry_no2];
						if (entry1.ready && entry2.ready) {
							y.execute();
							y.i.lastCycle = cycle + y.latency - 1;
							scoreBoard.instructions.put(y.i,
									scoreBoard.instructions.get(y.i)
											+ ",Excuted :" + y.i.lastCycle);
							Writebs.add(y);
							tmp.add(y);
							// Excute.remove(i);

						}

					} else if (y.qj != null) {
						int entry_no1 = Integer.parseInt(y.qj);
						Entry entry1 = scoreBoard.rob[entry_no1];
						if (entry1.ready) {
							y.execute();
							y.i.lastCycle = cycle + y.latency - 1;
							scoreBoard.instructions.put(y.i,
									scoreBoard.instructions.get(y.i)
											+ ",Excuted :" + y.i.lastCycle);
							Writebs.add(y);
							tmp.add(y);

							// Excute.remove(i);

						}

					} else {
						// qk!=null hya elly mstnya
						int entry_no1 = Integer.parseInt(y.qk);
						Entry entry1 = scoreBoard.rob[entry_no1];
						if (entry1.ready) {
							y.execute();
							y.i.lastCycle = cycle + y.latency - 1;
							scoreBoard.instructions.put(y.i,
									scoreBoard.instructions.get(y.i)
											+ ",Excuted :" + y.i.lastCycle);
							Writebs.add(y);
							tmp.add(y);
							// Excute.remove(i);

						}

					}
					if (y.op.contains("beq")) {
						int entry_no = y.dest;
						Entry e = scoreBoard.rob[entry_no];
						if (e.value != null) {
							int val = Integer.parseInt(e.value);
							if ((val == 0 && Integer.valueOf(y.i.offset) > 0)
									|| (val != 0 && Integer.valueOf(y.i.offset) < 0)) {
								PC = y.i.PC + 1 + Integer.parseInt(y.i.offset);
								System.out.println("should flush");
								issue.clear();//la2ny lazm amsh elly ablya bas
								return;
							}
						}

					}

				}

			}

		}
		for (int i = 0; i < tmp.size(); i++) {
			Excute.remove(tmp.get(i));
		}
		tmp.clear();
		System.err.println("write back size is "+Writebs.size());
		if(Writebs.size()>0)
		writeBack(0);
	}

	// //end of excute
	// //////////////////////////////////////////////////weam
	// start of writeback
	public static void writeBack(int cycle) {
		System.err.println("iam in in the wb "+Writebs.size());
		scoreBoard.print_scoreboard();
		ArrayList <FU>delete=new ArrayList<FU>();
		for (int i = 0; i < Writebs.size(); i++) {
			System.err.println(" iam trying to write  hhhh");
			FU temp = (FU) Writebs.get(i);
			System.out.println("dakhlt fel loopp  "+temp);
			int robentry = temp.dest;
			Entry ent = scoreBoard.rob[robentry];
			Instruction ins = temp.i;
			if (ins != null && ent!=null &&temp!=null) {
				//System.out.println("ana hena");
				String status = scoreBoard.instructions.get(ins);
				if (status != null && !ent.ready) {
					//System.out.println("anaa hena");
					ent.ready = true; // make the entry in rob ready
					temp.wb(); // put the value of execution in the value column
								// in rob
					// check dependencies on that rob so I will loop on the
					// arraylist of functions and check if qj or qk = robentry
					/*for (int j = 0; j < Excute.size(); j++) {
						// //// mmkn ykon felly fo2 moshkla
						FU temp2 = (FU) Excute.get(j);
						if (temp2.qj != null
								&& Integer.parseInt(temp2.qj) == robentry) {
							temp2.qj = null;
							// put the value resulted when rob is ready to
							// either vj or vk;
							temp2.vj = ent.dest;

						} else if (temp2.qj != null
								&& Integer.parseInt(temp.qk) == robentry) {
							temp2.qk = null;
							temp2.vk = ent.dest;

						}
						calculate_address(4);
						Excute(5);
						Issue(1); // lazem issue bas msh 3rfa eh el
						// instruction
					}*/
					
					// change in instructions hashtable to writeback
					if(cycle>temp.i.lastCycle+1){
						status = status+" , "+"WrittenBack"+cycle;
					}
					else {
						temp.i.lastCycle=temp.i.lastCycle+1;
						status = status+" , "+"WrittenBack"+temp.i.lastCycle;
					}
					scoreBoard.instructions.put(ins, status);
					FU f = new FU(temp.type, temp.name, temp.latency); // temporary
																		// FU
																		// having
																		// the
																		// same
																		// attributes
																		// of
																		// temp
																		// in
																		// order
																		// not
																		// to be
																		// cleared
																		// by
																		// clearing
																		// the
																		// temp
					f.busy = temp.busy;
					f.op = temp.op;
					f.vj = temp.vj;
					f.vk = temp.vk;
					f.qj = temp.qj;
					f.qk = temp.qk;
					f.a = temp.a;
					f.dest = temp.dest;
					f.i = temp.i;
					
					
//					calculate_address(4);
//					Excute(5);
//					Issue(1);
					delete.add(temp);
					commits.add(f); // this
					System.err.println("lololololyyy i added to commit");
					// clear the FU from the list

					System.out.println("For writing");

					temp.clearFU();
					//scoreBoard.print_scoreboard();
				///Writebs.remove(i);
					System.out.println("Ned5ol 3la el commit b2a");
//					if(commits.size()>0)
//						commit(0);

				}
			}
		}
		for (int i = 0; i <delete.size(); i++) {
			Writebs.remove(delete.get(i));
			}
		delete.clear();
		calculate_address(4);
		Excute(5);
		Issue(1);
		if(commits.size()>0)
			commit(0);
	}

	// end of writeback
	// start of commit
		public static void commit( int cycle ) {
			//for (int i = 0; i < commits.size(); i++) {
			FU temp =null;
			boolean off =false;
			if(commits.size() > 0){
				System.err.println("commit ,size is greater than o "+commits.size());
				//FU temp = (FU) commits.get(i);
				int headpointer = scoreBoard.head;  // this is what is the head is pointing at .
				String dd = scoreBoard.rob[headpointer].dest; // this is the destination of the entry corresponding the head pointer
				
				for(int i =0 ; i<commits.size();i++){
				 temp= (FU) commits.get(i);
				 System.out.println("in commit :temp is :"+temp);
				if(temp!=null){
				int robentry = temp.dest;
				// System.out.println(temp.toString());
				Entry ent = scoreBoard.rob[robentry];
				Instruction ins = temp.i;

				// System.out.println(ent.dest);
				String status = scoreBoard.instructions.get(ins);
				System.err.println("header pointer is "+headpointer+" rob entry "+robentry);
				if (headpointer == robentry) {
					//System.out.println(ent.dest);
					scoreBoard.registerStatus.put(ent.dest, 0); // clear the Rob
																// number from
																// register status
					ent.clear(); // clear entry from ROB
					if(cycle>temp.i.lastCycle+1){
						status =status+" , "+" Committed "+cycle; // change the status
					}
					else {
						temp.i.lastCycle=temp.i.lastCycle+1;
						status =status+" , "+" Committed "+temp.i.lastCycle;
					}
						
					scoreBoard.instructions.put(ins, status);
					scoreBoard.head = scoreBoard.increment(scoreBoard.head); // increment head pointer
					commits.remove(temp);
					off=true;
					System.err.println("i can commit now ");
					break;

				} 
				
			}
				}
				scoreBoard.print_scoreboard();
				if(off){
				if(commits.size()>0)
					commit(temp.i.lastCycle+1);
				if(issue.size()>0)
					Issue(temp.i.lastCycle+1);
				}
			
		}

		}

		// end of commit
	// /////////////////////////////////weam

	// //method that take block no in a cache and return block No in MM
	public static int getPhysicalAddressi(int BlockNo, Cache c) {
		String tmp;
		if (c.getAssoc() == c.getNoOFBlocks()) {// full associative
			if (c.getWritePolicy() == 0) {
				tmp = Integer
						.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c
								.getContent()[BlockNo].length - 2]));

				// +Integer.toBinaryString(c.offsetBits);
			} else {
				tmp = Integer
						.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c
								.getContent()[BlockNo].length - 3]));
				// Integer.toBinaryString(c.offsetBits);;
			}

		} else if (c.getAssoc() == 1) {// direct map
			String index = Integer.toBinaryString(BlockNo);
			while (index.length() < c.indexBits) {
				index = "0" + index;
			}
			System.out.println("index is " + index);
			System.out
					.println(c.getContent()[BlockNo][c.getContent()[BlockNo].length - 2]);
			if (c.getWritePolicy() == 0) {

				tmp = Integer
						.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c
								.getContent()[BlockNo].length - 2]))
						+ index;
				// Integer.toBinaryString(c.offsetBits);
				// tmp2=(Integer.parseInt(c.getContent()[BlockNo][c.getContent().length-2])/(1-(1/c.getNoOFBlocks())*c.getNoOFBlocks()));
			} else {
				// tmp2=(Integer.parseInt(c.getContent()[BlockNo][c.getContent().length-3])/(1-(1/c.getNoOFBlocks())*c.getNoOFBlocks()));

				tmp = Integer
						.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c
								.getContent()[BlockNo].length - 3]))
						+ index;
				// Integer.toBinaryString(c.offsetBits);;
			}

		} else {// set associative
			String index = Integer.toBinaryString(BlockNo / c.getAssoc());
			while (index.length() < c.indexBits) {
				index = "0" + index;
			}
			if (c.getWritePolicy() == 0) {

				System.out
						.println(c.getContent()[BlockNo][c.getContent()[BlockNo].length - 2]
								+ " block is " + BlockNo);
				System.out.println(index);
				tmp = Integer
						.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c
								.getContent()[BlockNo].length - 2]))
						+ index;

				// Integer.toBinaryString(c.offsetBits);

			} else {
				System.out
						.println(c.getContent()[BlockNo][c.getContent()[BlockNo].length - 3]
								+ " block is " + BlockNo);
				System.out.println(index);
				tmp = Integer
						.toBinaryString(Integer.parseInt(c.getContent()[BlockNo][c
								.getContent()[BlockNo].length - 3]))
						+ index;
				// Integer.toBinaryString(c.offsetBits);
			}

		}
		System.out.println("tmp is " + tmp);
		return Integer.parseInt(tmp, 2);

	}

	// this method update the block in all level of caches and the main memory
	public static void writeBackOrThrought(Cache c, int physicaladdress,
			int index) {
		String[] tmp = c.getContentOf(index);
		for (int i = 0; i < cacheLevel.size(); i++) {

			Cache c2 = cacheLevel.get(i);
			if (c2 != c) {
				System.err.println("cache no " + i + " ," + c2.lineSize
						+ ", assoc is " + c2.getAssoc());

				if (c2.getAssoc() == 1) {// direct map
					System.out.println("i am in a direct map");
					int index2 = physicaladdress % c2.getNoOFBlocks();
					int tag = physicaladdress / c2.getNoOFBlocks();
					System.err.println("iam in direct map tag is " + tag
							+ " index is " + index2);
					if (c2.getWritePolicy() == 0) {// write through
						if (c2.getContent()[index2][c2.getContent()[index2].length - 2] != null
								&& c2.getContent()[index2][c2.getContent()[index2].length - 2]
										.equals(String.valueOf(tag))) {
							System.out.println("direct map and foun it ");
							for (int j = 0; j < c2.getContent()[index2].length - 2; j++) {// write
																							// the
																							// new
																							// block
																							// byte
																							// by
																							// byte
								c2.getContent()[index2][j] = tmp[j];
								System.out.println("copying in direct map"
										+ tmp[j]);
							}

						}

					} else {// write back
						if (c2.getContent()[index2][c2.getContent()[index2].length - 3] != null
								&& c2.getContent()[index2][c2.getContent()[index2].length - 3]
										.equals(String.valueOf(tag))) {
							for (int j = 0; j < c2.getContent()[index].length - 3; j++) {
								c2.getContent()[index2][j] = tmp[j];
							}

						}

					}
				}

				else if (c2.getAssoc() == c2.getNoOFBlocks()) {// full
																// associative
					System.err.println("i am full associative ");
					int index3 = -1;
					int tag = physicaladdress;
					if (c2.getWritePolicy() == 0) {

						for (int j = 0; j < c2.getContent().length; j++) {// search
																			// in
																			// all
																			// blocks
							if (c2.getContent()[j][c2.getContent()[j].length - 2] != null
									&& c2.getContent()[j][c2.getContent()[j].length - 2]
											.equals(String.valueOf(tag))) {// search
																			// for
																			// the
																			// block
																			// in
																			// the
																			// whole
																			// cache
								index3 = j;
								System.err.println("found it !");
								break;
							}

						}
						if (index3 != -1) {

							for (int j = 0; j < c2.getContent()[index3].length - 2; j++) {
								c2.getContent()[index3][j] = tmp[j];
								System.err.println("copying .." + tmp[j]
										+ "value of i is " + j);
							}

						}
					}

					else {// write back
						for (int j = 0; j < c2.getContent().length; j++) {
							if (c2.getContent()[j][c2.getContent()[j].length - 3] != null
									&& c2.getContent()[j][c2.getContent()[j].length - 3]
											.equals(String.valueOf(tag))) {// search
																			// for
																			// the
																			// block
																			// in
																			// the
																			// whole
																			// cache
								index3 = j;
								System.out
										.println("direct map write back and found it ");
								break;
							}

						}
						if (index3 != -1) {
							for (int j = 0; j < c2.getContent()[index3].length - 3; j++) {
								c2.getContent()[index3][j] = tmp[j];
								System.out
										.println("copying direct map write back"
												+ j + " value " + tmp[j]);
							}

						}
					}

				} else {// set associative
					System.out.println("iam set **");
					int no_of_sets = c2.getNoOFBlocks() / c2.getAssoc();
					int set_no = physicaladdress % no_of_sets;
					int tag = physicaladdress / no_of_sets;
					int startBlock = set_no
							* (c2.getNoOFBlocks() / c2.getAssoc());
					int index3 = -1;
					System.out.println("tag is " + tag + " start block is  "
							+ startBlock);

					// //////////////
					if (c2.getWritePolicy() == 0) {

						for (int j = 0; j < c2.getAssoc(); j++) {
							if (c2.getContent()[startBlock][c2.getContent()[j].length - 2] != null
									&& c2.getContent()[startBlock][c2
											.getContent()[0].length - 2]
											.equals(String.valueOf(tag))) {
								index3 = startBlock;
								System.out.println("iam set and i found it ");
								break;

							}
							++startBlock;
						}
						if (index3 != -1) {
							System.err.println(" i found it copy in "
									+ startBlock);

							for (int j = 0; j < c2.getContent()[startBlock].length - 2; j++) {
								c2.getContent()[startBlock][j] = tmp[j];
								System.out.println("coping in the set "
										+ tmp[j] + " ,j is " + j);

							}
						}
					} else {

						for (int j = 0; j < c2.getAssoc(); j++) {
							if (c2.getContent()[j][c2.getContent()[j].length - 3] != null
									&& c2.getContent()[startBlock][c2
											.getContent()[0].length - 3]
											.equals(String.valueOf(tag))) {
								index3 = startBlock;
								break;
							}
							++startBlock;
						}
						if (index3 != -1) {
							for (int j = 0; j < c2.getContent()[startBlock].length - 3; j++) {
								c2.getContent()[startBlock][j] = tmp[j];

							}
						}

					}
					// /////////////////////

				}
			}
		}

	}

	public static void main(String[] args) {
		registers.put("F0", 0);
		registers.put("F1", 1);
		registers.put("F2", 2);
		registers.put("F3", 3);
		registers.put("F4", 4);
		registers.put("F5", 5);
		Processor p = new Processor();
		Instruction b = new Instruction("add", "F0", "F1", "F2", "add", "2");
		b.set_pc(1);
		Instruction a = new Instruction("mul", "F2", "F4", "F5", "mul", null);
		//Instruction c = new Instruction("load", "F3", "F5", "F3", "load", "0");
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		issue.add(b);
		issue.add(a);
		// list.add(c);
		Issue(1);
		System.out.println("before last print ");
		scoreBoard.print_scoreboard();
		System.out.println();

		/*
		 * Cache c1=new Cache(4,1,1,1,1);//direct cacheLevel.add(c1);
		 * 
		 * Cache c2=new Cache(2,1,2,1,1);//full
		 * //System.err.println(c2.getContent()[0].length); cacheLevel.add(c2);
		 * 
		 * Cache c3=new Cache(4,1,2,1,1);//set cacheLevel.add(c3);
		 * 
		 * String [][] x2=c2.getContent(); x2[1][x2[1].length-3]="5";
		 * 
		 * 
		 * 
		 * String [][] x3=c3.getContent(); x3[3][x3[1].length-3]="2";
		 * 
		 * String [][] x=c1.getContent(); x[1][x[1].length-3]="1";
		 * 
		 * int phys1=getPhysicalAddressi(1, c2);
		 * System.out.println("phys address is "+ phys1); for (int i = 0; i <
		 * x2[1].length-3; i++) {
		 * 
		 * x2[1][i]=String.valueOf(i); System.out.println("x3 is   "+x3[3][i]);
		 * }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * writeBackOrThrought(c2,phys1,1);
		 * 
		 * for (int i = 0; i < x[1].length-3; i++) {
		 * System.out.println(" i is "+ i +" x  "+x[1][i]); }
		 */

		// System.out.println("x is   "+x[1][x[1].length-2]);

		// System.out.println(" where iam storing "+ (x2.length-2));

		// System.out.println(" hh  "+x3[1][x3[1].length-2]);

		//
		// for (int i = 0; i < c2.getContent()[1].length; i++) {
		// System.err.println("x2  "+c2.getContent()[1][i] +" "+ i);
		// }

		// x[0][x.length-2]="2";

		// System.out.println(getPhysicalAddressi(0, c1));

		/*
		 * BufferedReader bf = new BufferedReader(new
		 * InputStreamReader(System.in));
		 * 
		 * System.out.println("What is the number of caches");
		 * 
		 * // take the number of caches first int numberOfCaches; try {
		 * numberOfCaches = Integer.parseInt(bf.readLine()); } catch (Exception
		 * e) { System.out.println("Input error"); e.printStackTrace(); return;
		 * }
		 * 
		 * System.out.println("Now for every cache what are the parameters?");
		 * System.out.println("Format: Size,Line Size, Associativity");
		 * 
		 * // use the number of caches given in the first line to create
		 * multiple caches with the given structure for (int i = 0; i <
		 * numberOfCaches; i++) { String[] line; try { // input structure
		 * assumed is // int,int,int NOT (int,int,int) // Another format could
		 * be used which takes 5 parameters instead of 3
		 * 
		 * // clear all spaces line =
		 * bf.readLine().replaceAll("\\s","").split(",");
		 * 
		 * int [] parsedLine = new int [line.length]; for (int j = 0; j <
		 * line.length; j++) { parsedLine[j] = Integer.parseInt(line[j]); }
		 * 
		 * // this will change is the 5 parameter constructor was used // this
		 * adds a new cache to the processor's arraylist of caches
		 * cacheLevel.add(new Cache(parsedLine[0], parsedLine[1],
		 * parsedLine[2]));
		 * 
		 * } catch (IOException e) {
		 * System.out.println("Error when reading cache line");
		 * e.printStackTrace(); } }
		 * 
		 * // add the memory level after all caches are created
		 * System.out.println("What is the memory access time?"); int
		 * memoryTime; try { memoryTime = Integer.parseInt(bf.readLine()); }
		 * catch (Exception e) { System.out.println("Wrong input");
		 * e.printStackTrace(); return; }
		 * 
		 * cacheLevel.add(new Cache(64*1024, 16, 1, 0, memoryTime));
		 */
	}

}
