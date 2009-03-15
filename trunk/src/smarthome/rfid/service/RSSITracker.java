package smarthome.rfid.service;

import java.util.Map;
import java.util.TreeMap;


import smarthome.rfid.data.AntennaList;
import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.data.Vector;

public class RSSITracker {
	private Map<Integer, Map<Integer, RSSIReading>> data;
	private int numAntennas;
	private int maxInterval;
	
	public RSSITracker(int numAntennas, int maxInterval) {
		this.data = new TreeMap<Integer, Map<Integer, RSSIReading>>();
		this.numAntennas = numAntennas;
		this.maxInterval = maxInterval;
	}
	
	public void logRssi(int tagId, int antennaId, RSSIReading reading) {
		if (data.get(tagId) == null) {
			data.put(tagId, new TreeMap<Integer, RSSIReading>());
		}
		data.get(tagId).put(antennaId, reading);
	}
	
	public Vector getSignalStrength(int tagId) {
		Map<Integer, RSSIReading> readings = data.get(tagId);		
		double[] rssi = new double[numAntennas];
		if (readings == null) return new Vector(rssi);
		long time = System.currentTimeMillis();
		for (Integer antennaId : readings.keySet()) {
			RSSIReading reading = readings.get(antennaId);
			if (time - reading.time() <= maxInterval) {
				rssi[antennaId - 1] = reading.rssi();
			}
		}
		return new Vector(rssi);
	}
	
	public int[] getTags() {
		int[] tags = new int[data.size()];
		int i = 0;
		for (Integer tagId : data.keySet()) {
			tags[i++] = tagId;
		}
		return tags;
	}
}
