package smarthome.rfid.data;

public class TrainingPoint {

	private Location loc;
	private double orientation;
	private Vector signalStrength; 
	
	public TrainingPoint (Location loc, double orientation, Vector signalStrength) {
		this.loc = loc;
		this.orientation=orientation;
		this.signalStrength = signalStrength; 
	}	
	
	public Location location() {
		return loc;
	}
	
	public double orientation() {
		return orientation;
	}
	
	public Vector signalStrength() {
		return signalStrength;
	}
	
	public String toString() {
		return "Training point: Location " + loc.toString() + ", Orientation " + orientation + ", RSSI " + signalStrength.toString();
	}
}
	
