package machineCode;
import java.io.*;
import javax.comm.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Main {
	
	private static int currentRSSI = 0;
	private static Queue<Integer> prevRSSI = new LinkedList<Integer>();
	
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
		e.nextElement();		
		CommPortIdentifier myIdentifier = (CommPortIdentifier)e.nextElement(); // myChooser.getSelectedIdentifier();
		System.out.println(myIdentifier.getName());
		System.out.println(myIdentifier.getPortType());
		System.out.println(myIdentifier.isCurrentlyOwned());
		
		final TestView tv = new TestView(null);
		Adapter adapter = new Adapter(myIdentifier, new AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {
				if (tagID == 191) {
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
						Main.currentRSSI = rssi;
						tv.update();
					}									
				}
			}
		});
		
		//adapter.resetNetwork();
		//adapter.disableAutoPolling();
		adapter.enableAutoPolling();
		
		tv.setVisible(true);
	}
	public static int getCurrentRSSI() {
		return currentRSSI;
	}
}
