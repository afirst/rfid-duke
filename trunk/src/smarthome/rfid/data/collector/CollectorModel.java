package smarthome.rfid.data.collector;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.service.RSSITracker;
import smarthome.rfid.data.Vector;


/*
 * CollectorModel is the state for the gui. Contains hashMap of
 * LocationTuple to DataTuple.
 * 
 */

public class CollectorModel {

	private int numAntennas;
	private RSSITracker tracker;
	private TrainingPointList list;

	public CollectorModel (int numAntennas) {
		this.numAntennas = numAntennas;
		tracker = new RSSITracker(numAntennas, Settings.POLL_INTERVAL);		
		list = new TrainingPointList();
	}
	
	public synchronized void clear() {
		tracker = new RSSITracker(numAntennas, 5000);
	}
	
	public synchronized void log(int tagId, int antennaId, int rssi) {
		tracker.logRssi(tagId, antennaId, new RSSIReading(rssi));
	}
	
	public synchronized void logPoint(int x, int y, int floor, int orientation, int tagNumber) {
		Vector ss = tracker.getSignalStrength(tagNumber);		
		TrainingPoint pt = new TrainingPoint(new Location(x, y, floor), orientation, ss);
		list.add(pt);		
	}
	
	public synchronized void clearPoints() {
		list.clear();		
	}
		
	public synchronized void loadFile (String file) {
		list.clear();
		try {
			list.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public synchronized void outputToFile (String file) {
		try {
			list.save(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}	
	
	public synchronized void removeAt(int index) {
		list.remove(index);
	}
	
	public Iterator<TrainingPoint> iterator() {
		return list.iterator();
	}
	
	public Vector getRSSI(int tag) {
		return tracker.getSignalStrength(tag);
	}
}
