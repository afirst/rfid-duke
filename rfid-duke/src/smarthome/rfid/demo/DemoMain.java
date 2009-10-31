package smarthome.rfid.demo;
import javax.comm.*;

import smarthome.rfid.LRX201Adapter;
import smarthome.rfid.LRX201AdapterListener;
import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.service.RSSITracker;

import java.text.DateFormat;
import java.util.*;

public class DemoMain {
	
	private static final int NUM_ANTENNAS = 5;
	private static int currentRSSI = 0;
	private static RSSITracker tracker = new RSSITracker(NUM_ANTENNAS, 10000);
	private static int readNum = 0;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		//PortChooser myChooser = new PortChooser(null); 
		//myChooser.setVisible(true);// blocking wait
		
		//String drivername = "com.sun.comm.Win32Driver";
		String drivername = "com.sun.comm.LinuxDriver";
		try {
			//CommDriver driver = (CommDriver) Class.forName(drivername).newInstance();
			//driver.initialize();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Enumeration e = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier myIdentifier = (CommPortIdentifier)e.nextElement(); // myChooser.getSelectedIdentifier();
		System.out.println(myIdentifier.getName());
		System.out.println(myIdentifier.getPortType());
		System.out.println(myIdentifier.isCurrentlyOwned());

		final long start = System.currentTimeMillis();
		LRX201Adapter adapter = new LRX201Adapter(myIdentifier, new LRX201AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {				
				tracker.logRssi(tagID, antennaID, new RSSIReading(rssi));
				int[] tags = tracker.getTags();
				String time = DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis() - start));
				for (int tag : tags) {
					System.out.println(readNum + ":\t" + time + ":\t" + tag + " :\t" + tracker.getSignalStrength(tag));
				}
				readNum++;
			}
		});	
		adapter.start(NUM_ANTENNAS);
	}
	public static int getCurrentRSSI() {
		return currentRSSI;
	}
}
