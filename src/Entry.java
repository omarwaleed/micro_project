
public class Entry {
    String type;
    String dest;
    String value;
    boolean ready;
    boolean occupied;
    public Entry(String t,String d,String v, boolean r) {
    	type = t;
    	dest = d;
    	value = v;
    	ready = r;
    	occupied = false;
    }
    public void clear() { // method that clears an entry
		type = "";
		value = "";
		dest = "";	
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
