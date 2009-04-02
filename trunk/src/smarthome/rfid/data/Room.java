package smarthome.rfid.data;

public class Room implements Comparable {
	public static final Room OUTSIDE = new Room("Outside");
	
	private double x;
	private double y;
	private double z;
	private String name;
	
	public Room(String name) {
		this.name = name;
	}
	
	public Room(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return "("+name+")";
	}
	
	public static void main(String[] args) {
		Room r = new Room("media");
		U.Print(r);
	}

	public int compareTo(Object o) {
		return this.name.compareTo(((Room)o).name);
	}

}
