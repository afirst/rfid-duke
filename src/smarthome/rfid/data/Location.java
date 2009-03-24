package smarthome.rfid.data;

public class Location extends Vector {	
	
	public static final Location UNKNOWN = new Location(Double.NaN, Double.NaN, Double.NaN);	
	
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
