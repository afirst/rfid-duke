package smarthome.rfid.service;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.comm.CommPortIdentifier;


import smarthome.rfid.DatabaseUpdater;
import smarthome.rfid.LRX201Adapter;
import smarthome.rfid.LRX201AdapterListener;
import smarthome.rfid.data.Algorithm;
import smarthome.rfid.data.Location;
import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.data.RoomMap;
import smarthome.rfid.data.SignalStrengthMovingAverages;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.Vector;
import smarthome.rfid.data.filters.TrainingPointMerger;

public class LocationService implements LRX201AdapterListener {
	private static final int QUEUE_SIZE = 1;
	
	private CommPortIdentifier comPort;
	private LRX201Adapter adapter;
	//private AntennaList antennas;
	private TrainingPointList trainingData;
	private RSSITracker rssiTracker;
	private SignalStrengthMovingAverages ssma;
	private boolean error = false;
	private String status = "";
	private LocationServiceListener listener;
	private Timer updateTimer;
	private DatabaseUpdater database;
	private Algorithm algorithm;
	private RoomMap roomMap; 
		
	public LocationService(String antennaFile, String trainingDataFile, int maxInterval, int updateInterval, Algorithm algorithm, CommPortIdentifier port, LocationServiceListener listener, int numAntennas, String roomMapAddress) {
		this.listener = listener;
		this.algorithm = algorithm;
		
		// create adapter
		comPort = port;
		adapter = new LRX201Adapter(comPort, this);
		
		// load training data
		//antennas = new AntennaList();		
		//try {
			//antennas.load(antennaFile);
		//} catch (FileNotFoundException e) {
			//logEvent("ERROR: Antenna file not found.", LocationServiceListener.ERROR);
			//return;
		//}
		trainingData = new TrainingPointList();
		roomMap = new RoomMap();
		try {
			roomMap.load(roomMapAddress);
		} catch (FileNotFoundException e1) {
			logEvent("ERROR: Room map file not found.", LocationServiceListener.ERROR);
			return;
		}
		
		try {
			trainingData.load(trainingDataFile);
		} catch (FileNotFoundException e) {
			logEvent("ERROR: Training data file not found.", LocationServiceListener.ERROR);
			return;
		}
		trainingData = (new TrainingPointMerger()).filter(trainingData);
		algorithm.setTrainingData(trainingData);
		
		// begin tracking
		database = new DatabaseUpdater();
		rssiTracker = new RSSITracker(numAntennas, maxInterval);
		ssma = new SignalStrengthMovingAverages(QUEUE_SIZE);
		adapter.start(numAntennas);
		updateTimer = new Timer();
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
	
	Location prevLocation;
	
	private void tick() {
		int[] tags = rssiTracker.getTags();
		for (int tagId : tags) {
			if (tagId == 101) {
			// get rssi values
			Vector rssi = rssiTracker.getSignalStrength(tagId);
			//Vector rssi = ssma.value(tagId);			
			logEvent("RSSI: " + rssiTracker.getSignalStrength(tagId), LocationServiceListener.GENERAL);
			System.out.println(rssi);
			
			// run through algorithm			
			Location location = algorithm.getLocation(tagId, rssi);
			/*
			if (prevLocation != null && RFIDMath.getDistance(location, prevLocation) > 0.35) {
				Location a = new Location(location.x() - prevLocation.x(), location.y() - prevLocation.y(), location.z() - prevLocation.z());
				a.scale(0.35 / RFIDMath.getDistance(a, new Location()));
				prevLocation.add(a);
				location = prevLocation;
			}*/
			
			System.out.println("location "+location + " " + roomMap.getRoom(location));
			
			// update in database			
			if (location.equals(Location.UNKNOWN)) {
				//logEvent("Set away " + tagId, LocationServiceListener.TAG);
				database.setAway(tagId);
			}
			else {
				//logEvent("Set home " + tagId + ": " + location, LocationServiceListener.TAG);
				location = new Location(location.x(), location.y(), location.z());
				location.add(new Vector(new double[] {0, 0, -1}));
				database.setHome(tagId, location);
			}
			}
		}
	}
	
	public void readTag(int antennaId, int tagId, int rssi) {
		rssiTracker.logRssi(tagId, antennaId, new RSSIReading(rssi));
		ssma.push(tagId, rssiTracker.getSignalStrength(tagId));
		//logEvent("Tag read: tagId " + tagId + ", antennaId " + antennaId + ", rssi " + rssi, LocationServiceListener.TAG);
	}
	
	public String getStatus() {
		return status;
	}
	
	public boolean error() {
		return error;
	}
}
