package smarthome.rfid.service;

import javax.comm.CommPortIdentifier;

import smarthome.rfid.PortChooser;
import smarthome.rfid.data.Algorithm;
import smarthome.rfid.data.FilteredKNN;
import smarthome.rfid.data.NearestNeighbor;
import smarthome.rfid.data.KNearestNeighbor;
import smarthome.rfid.data.RegressionAlgorithm;

public class Main {
	public static final int NUM_ANTENNAS = 5;
	private static final String ANTENNA_FILE = "";
	private static final String TRAINING_DATA_FILE = "data.txt";
	private static final int MAX_INTERVAL = 5000;
//<<<<<<< .mine
	//private static final int UPDATE_INTERVAL = 2000;
	//private static final Algorithm ALGORITHM = new FilteredKNN(4);
//=======
	private static final int UPDATE_INTERVAL = 5000;
	private static final Algorithm ALGORITHM = new RegressionAlgorithm(1);
	private static final CommPortIdentifier PORT = PortChooser.selectPort();
	private static final LocationServiceListener LISTENER = new LocationServiceListener() {
		public void serviceEvent(String detail, int eventType) {
			System.out.println(detail);
		}		
	};
	
	public static void main(String[] args) {
		new LocationService(ANTENNA_FILE, TRAINING_DATA_FILE, MAX_INTERVAL, UPDATE_INTERVAL, ALGORITHM, PORT, LISTENER, NUM_ANTENNAS);
	}
}
