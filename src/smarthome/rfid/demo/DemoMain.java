package smarthome.rfid.demo;
import java.io.*;
import javax.comm.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smarthome.rfid.LRX201Adapter;
import smarthome.rfid.LRX201AdapterListener;
import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.service.RSSITracker;

import java.util.*;

public class DemoMain {
	
	private static int currentRSSI = 0;
	private static Queue<Integer> prevRSSI = new LinkedList<Integer>();
	private static RSSITracker tracker = new RSSITracker(5, 10000);
	
	public static void main(String[] args) throws Exception {
		
		//PortChooser myChooser = new PortChooser(null); 
		//myChooser.setVisible(true);// blocking wait
		
		String drivername = "com.sun.comm.Win32Driver";
		try {
			CommDriver driver = (CommDriver) Class.forName(drivername).newInstance();
			driver.initialize();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Enumeration e = CommPortIdentifier.getPortIdentifiers();
		//e.nextElement();		
		CommPortIdentifier myIdentifier = (CommPortIdentifier)e.nextElement(); // myChooser.getSelectedIdentifier();
		System.out.println(myIdentifier.getName());
		System.out.println(myIdentifier.getPortType());
		System.out.println(myIdentifier.isCurrentlyOwned());
		
		LRX201Adapter adapter = new LRX201Adapter(myIdentifier, new LRX201AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {				
				DemoMain.this.tracker.logRssi(tagID, antennaID, new RSSIReading(rssi));
				int[] tags = tracker.getTags();
				for (int tag : tags) {
					System.out.println(tag + " : " + tracker.getSignalStrength(tag));
				}
				/*if (tagID == tagID) {
					System.out.println("Read tag: Antenna: " + antennaID + ", Tag: " + tagID + ", RSSI: " + rssi);
					prevRSSI.add(rssi);
					if (prevRSSI.size() > 2) {
						prevRSSI.remove();
					}
					int prevR = -1;
					boolean stable = true;
					for (int r : prevRSSI) {
						if (prevR > -1) {
							if (Math.abs(r - prevR) > 5) {
								stable = false;
							}
						}
						prevR = r;
					}
					if (stable) {
						DemoMain.currentRSSI = rssi;
						tv.update();
					}									
				}*/
			}
		});
	
		//adapter.setReceiverGain(255, 255, 2, true);
		
		//adapter.resetNetwork();
		//adapter.disableAutoPolling();
		//adapter.enableAutoPolling();
		//adapter.disableAutoPolling();
		adapter.start(5);
		//final DemoView tv = new DemoView(null);		
		//tv.setVisible(true);
	}
	public static int getCurrentRSSI() {
		return currentRSSI;
	}
}
