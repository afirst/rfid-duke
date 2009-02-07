package smarthome.rfid.data;

public class Antenna {
	public int antennaId;
	public Location location;

	public Antenna (int antennaId, Location location) {
		this.antennaId = antennaId;
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "Antenna " + antennaId + ", Location " + location.toString(); 
	}
}
