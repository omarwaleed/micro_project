public class Entry {
    String type;//of the instruction is it load/add etc
    String dest;//name of destination register
    String value;//value of destination register 
    boolean ready;
    boolean occupied;
    public Entry(){
    	
    }
    public Entry(String t,String d) {
    	type = t;
    	dest = d;
    	//value = v;
    	ready = false;
    	occupied = false;
    }
    public void setValue(String v){
    	value=v;
    }
    public void setReady(){
    	ready=true;
    	
    }
    public void clear() { // method that clears an entry
		type = null;
		value = null;
		dest = null;	
	    ready = false;
	    occupied = false;
	}
	public String toString() {
		return "[Type: " + type + ", Dest: " + dest + ", Value: " + value + " ,Ready: " + ready + " ,Occupied:"  + occupied + "]";
	}
	public boolean equals(Entry e) {
		return (e.occupied == occupied && e.ready == ready && e.value.equals(value) && e.type.equals(type) && e.dest
				.equals(dest));

	}
}