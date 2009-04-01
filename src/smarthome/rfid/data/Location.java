package smarthome.rfid.data;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import smarthome.rfid.data.collector.RoomMap;

public class Location extends Vector {	
	
	public static final Location UNKNOWN = new Location(Double.NaN, Double.NaN, Double.NaN);	
	public RoomMap map; 
	
	public Location (double x, double y, double z) {
		super(new double[] {x, y, z});
		map = new RoomMap();
	}	
	
	public Location() {
		super(3);
	}
	
	public double x() {
		return vector[0];
	}
	
	public double y() {
		return vector[1];
	}
	
	public double z() {
		return vector[2];
	}
	
	public Room getRoom() throws FileNotFoundException {
		map.load("room-map.txt");
		for(Rectangle r : map.keySet()) {
			if (r.contains(this)) {
				return map.get(r);
			}
		}
		return new Room("Outside");
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Location loc = new Location(.4,.2,2);
		System.out.println(loc.getRoom());
	}
}
