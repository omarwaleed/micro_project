
public class Entry {
    private String type;
    private String dest;
    private String value;
    private boolean ready;
    private boolean occupied;
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
                                       /*Getters and Setters down here*/
	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
    
}
