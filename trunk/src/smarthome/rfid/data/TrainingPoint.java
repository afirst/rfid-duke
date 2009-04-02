package smarthome.rfid.data;

import java.util.HashMap;

public class TrainingPoint {

	private Location loc;
	private double orientation;
	private SignalStrength signalStrength;
	//private HashMap<String, Object> userData;
	
	public TrainingPoint (Location loc, double orientation, SignalStrength signalStrength) {
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
	
	public SignalStrength signalStrength() {
		return signalStrength;
	}
	
	public String toString() {
		return "Training point: Location " + loc.toString() + ", Orientation " + orientation + ", RSSI " + signalStrength.toString();
	}
	
	/*public HashMap<String, Object> data() {
		if (this.userData == null) {
			userData = new HashMap<String, Object>();			
		}
		return userData;
	}*/
	
	@Override
	public boolean equals(Object obj) {
		TrainingPoint pt = (TrainingPoint)obj;
		if (!(loc.equals(pt.loc) && orientation == pt.orientation && signalStrength.equals(pt.signalStrength))) {
			return false;
		}
		/*if (pt.userData == userData) return true;
		if (pt.userData == null || userData == null) {
			if (pt.userData == null && userData.size() == 0) return true;
			if (userData == null && pt.userData.size() == 0) return true;
			return false;
		}
		if (pt.userData.size() != userData.size()) return false;
		for (String s : pt.userData.keySet()) {
			if (!userData.containsKey(s)) return false;
			if (!pt.userData.get(s).equals(userData.get(s))) return false;
		}*/
		return true;
	}
	
	@Override
	public int hashCode() {
		int h = 0;
		h += loc.hashCode() + new Double(orientation).hashCode() + signalStrength.hashCode();
		/*if (userData == null || userData.size() == 0) {
			return h;
		}
		h += new Integer(userData.size()).hashCode();
		for (String s : userData.keySet()) {
			h += s.hashCode();
			h += userData.get(s).hashCode();
		}*/
		return h;
	}
}
	
