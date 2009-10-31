package smarthome.rfid.data;

public class RSSIReading {
	private int rssi;
	private long time;	
	
	public RSSIReading(int rssi) {
		this(rssi, System.currentTimeMillis());
	}
	
	public RSSIReading(int rssi, long time) {
		this.rssi = rssi;
		this.time = time;
	}
	
	public int rssi() {
		return rssi;
	}
	
	public long time() {
		return time;
	}
}
