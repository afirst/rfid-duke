package smarthome.rfid;

public interface LRX201AdapterListener {
	void readTag(int antennaId, int tagId, int rssi);		
}
