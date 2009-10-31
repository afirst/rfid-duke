package smarthome.rfid.data;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class Location extends Vector {	
	
	public static final Location UNKNOWN = new Location(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);	
	
	public Location (double x, double y, double z) {
		super(new double[] {x, y, z});
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
}
