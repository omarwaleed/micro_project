// this class handles the processor functions and contains all the processor registers

public class Processor {
	
	private static int[] register = new int[32];

//	get the value inside a register
	public int getRegister(int reg) 
	{
		return register[reg];
	}

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
	 

}
