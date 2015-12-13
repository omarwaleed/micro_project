public class FU {
	String type; // floating point or int
	String name; // ex. load1 load2 add1
	String op;
	String val;
	boolean busy;
	int latency; // number of clock cycles it takes to execute
	String vj; // source register if ready
	String vk; // other source register if exists or ready
	String qj; // rob entry for source register if not ready
	String qk; // rob entry for other source register if not ready
	int dest; // rob entry
	String a; // offset for load and store instructions
	Instruction i;

	public FU(String t, String n, int l) {
		type = t;
		name = n;
		latency = l;
		busy = false;
	}

	public String toString() {
		return "[Type " + type + ", Name: " + name + ", OP: " + op + ", Busy: "
				+ busy + ", Latency: " + latency + ", Vj: " + vj + ", Vk: "
				+ vk + ", Qj: " + qj + ", Qk: " + qk + ", Dest: " + dest
				+ ", A: " + a + "]";
	}

	public void execute() {
		//System.out.println();
		Entry entry = Processor.scoreBoard.rob[dest]; // el mkan elly hakhzn feeh el
											// result
		val = null;
		if (op.equalsIgnoreCase("load")) {
			// call load method
			String ana =Processor.cacheAccesRead(Integer.parseInt(a), true);
			String [] ana1=ana.split("-");
			val=ana1[0];
			String overhead=ana1[1];
			i.lastCycle=i.lastCycle+Integer.parseInt(overhead);

		} else if (op.equalsIgnoreCase("store")) {
			// call store method
			String[]x={String.valueOf(Processor.registers.get(vj))};
			int xx=Processor.cacheAccessWrite(Integer.parseInt(a),x , true);
			i.lastCycle=i.lastCycle+xx;
			
		} else if (op.equalsIgnoreCase("add")) {
			// add method
			if (qj == null && qk == null) {// they are in the registers
				int result = Processor.registers.get(vj)
						+ Processor.registers.get(vk);
				val = String.valueOf(result);

			} else {// go and take it from the ROB
				int result;
				if (qj != null) {// i have to get vj from the ROB
					Entry entry1 = Processor.scoreBoard.rob[Integer.parseInt(qj)];

					result = Processor.registers.get(vk)
							+ Integer.parseInt(entry1.value);

				} else {// qk is not null
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qk)];
					result = Processor.registers.get(vj)
							+ Integer.parseInt(entry1.value);
				}

				val = String.valueOf(result);
			}

		} else if (op.equalsIgnoreCase("sub") || op.equalsIgnoreCase("beq")) {
			if (qj == null && qk == null) {// they are in the registers
				int result = Processor.registers.get(vj)
						- Processor.registers.get(vk);
				val = String.valueOf(result);

			} else {// go and take it from the ROB
				int result;
				if (qj != null) {// i have to get vj from the ROB
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qj)];

					result = Integer.parseInt(entry1.value)
							- Processor.registers.get(vk);

				} else {// qk is not null
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qk)];
					result = Processor.registers.get(vj)
							- Integer.parseInt(entry1.value);
				}

				val = String.valueOf(result);
			}

		} else if (op.equalsIgnoreCase("mult")) {
			if (qj == null && qk == null) {// they are in the registers
				int result = Processor.registers.get(vj)
						* Processor.registers.get(vk);
				val = String.valueOf(result);

			} else {// go and take it from the ROB
				int result;
				if (qj != null) {// i have to get vj from the ROB
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qj)];

					result = Integer.parseInt(entry1.value)
							* Processor.registers.get(vk);

				} else {// qk is not null
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qk)];
					result = Processor.registers.get(vj)
							* Integer.parseInt(entry1.value);
				}

				val = String.valueOf(result);
			}

		} else if (op.equalsIgnoreCase("addi")) {
			if (qj == null) {// they are in the registers
				int result = Processor.registers.get(vj)
						+ Integer.parseInt(a);
				val = String.valueOf(result);

			} else {
				Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qj)];
				int result = Integer.parseInt(entry1.value)
						+ Integer.parseInt(a);
				val = String.valueOf(result);

			}

		} else if (op.equalsIgnoreCase("nand")) {
			if (qj == null && qk == null) {// they are in the registers
				int result = ~(Processor.registers.get(vj) & Processor.registers
						.get(vk));
				val = String.valueOf(result);

			} else {// go and take it from the ROB
				int result;
				if (qj != null) {// i have to get vj from the ROB
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qj)];

					result = ~(Integer.parseInt(entry1.value) & Processor.registers
							.get(vk));

				} else {// qk is not null
					Entry entry1 =  Processor.scoreBoard.rob[Integer.parseInt(qk)];
					result = ~(Processor.registers.get(vj) & Integer
							.parseInt(entry1.value));
				}

				val = String.valueOf(result);

			}
		}
		// entry.value=val;
		// add other instructions
	}

	// //////////////////weam
	public void wb() {
		System.err.println("val is " + val);
		String value = val;
		// String value="15"; //for testing purpose;
		if (value != null) {
			Entry entry =  Processor.scoreBoard.rob[dest]; // el mkan elly hakhzn feeh el
												// result
			entry.value = value;
			// System.out.println(entry.dest);
			System.out.println("entryyyyyy"+entry);
			if(entry.dest!=null)
			Processor.registers.put(entry.dest, Integer.parseInt(value));
		}
	}

	// this method to clear the FU
	public void clearFU() {
		op = null;
		busy = false;
		vj = null; // source register if ready
		vk = null; // other source register if exists or ready
		qj = null; // rob entry for source register if not ready
		qk = null; // rob entry for other source register if not ready
		dest = 0; // rob entry
		a = null; // offset for load and store instructions

	}
	// ///////////end weam

}
