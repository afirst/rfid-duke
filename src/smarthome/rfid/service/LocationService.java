package smarthome.rfid.service;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.comm.CommPortIdentifier;


import smarthome.rfid.DatabaseUpdater;
import smarthome.rfid.LRX201Adapter;
import smarthome.rfid.LRX201AdapterListener;
import smarthome.rfid.PortChooser;
import smarthome.rfid.data.Algorithm;
import smarthome.rfid.data.AntennaList;
import smarthome.rfid.data.Location;
import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.Vector;

public class LocationService implements LRX201AdapterListener {
	private CommPortIdentifier comPort;
	private LRX201Adapter adapter;
	private AntennaList antennas;
	private TrainingPointList trainingData;
	private RSSITracker rssiTracker;
	private boolean error = false;
	private String status = "";
	private LocationServiceListener listener;
	private Timer updateTimer;
	private DatabaseUpdater database;
	private Algorithm algorithm;
		
	public LocationService(String antennaFile, String trainingDataFile, int maxInterval, int updateInterval, Algorithm algorithm, CommPortIdentifier port, LocationServiceListener listener) {
		this.listener = listener;
		this.algorithm = algorithm;
		
		// create adapter
		comPort = port;
		adapter = new LRX201Adapter(comPort, this);
		
		// load training data
		antennas = new AntennaList();		
		try {
			antennas.load(antennaFile);
		} catch (FileNotFoundException e) {
			logEvent("ERROR: Antenna file not found.", LocationServiceListener.ERROR);
			return;
		}
		trainingData = new TrainingPointList();
		try {
			trainingData.load(trainingDataFile);
		} catch (FileNotFoundException e) {
			logEvent("ERROR: Training data file not found.", LocationServiceListener.ERROR);
			return;
		}
		algorithm.setTrainingData(trainingData, antennas);
		
		// begin tracking
		database = new DatabaseUpdater();
		rssiTracker = new RSSITracker(antennas.size(), maxInterval);
		adapter.start(5);	
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tick();
			}			
		}, maxInterval, updateInterval);		
	}
	
	private void logEvent(String event, int type) {
		this.status = event;
		if (type == LocationServiceListener.ERROR) this.error = true;
		if (listener != null) listener.serviceEvent(event, type);
	}
	
	private void tick() {
		int[] tags = rssiTracker.getTags();
		for (int tagId : tags) {
			
			// get rssi values
			Vector rssi = rssiTracker.getSignalStrength(tagId);
			
			// run through algorithm
			Location location = algorithm.getLocation(tagId, rssi);
			
			// update in database
			if (location.equals(Location.UNKNOWN)) {
				database.setAway(tagId);
			}
			else {
				database.setHome(tagId, location);
			}
		}
	}
	
	public void readTag(int antennaId, int tagId, int rssi) {
		rssiTracker.logRssi(tagId, antennaId, new RSSIReading(rssi));
		logEvent("Tag read: tagId " + tagId + ", antennaId " + antennaId + ", rssi " + rssi, LocationServiceListener.TAG);
	}
	
	public String getStatus() {
		return status;
	}
	
	public boolean error() {
		return error;
	}
}
