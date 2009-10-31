package smarthome.rfid.data.collector;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;

import javax.comm.CommPortIdentifier;
import javax.swing.*;


import smarthome.rfid.LRX201Adapter;
import smarthome.rfid.LRX201AdapterListener;
import smarthome.rfid.PortChooser;


import java.util.*;

public class CollectorMain {
	private static final int NUM_ANTENNAS = 5;
	
	public static void main(String[] args) {				
		final CollectorModel model = new CollectorModel(NUM_ANTENNAS); 
		final CollectorView view = new CollectorView(model);
		//CollectorNetworkController net = new CollectorNetworkController(model);
		//net.start();
		
		PortChooser myChooser = new PortChooser(null); 
		myChooser.setVisible(true);// blocking wait
		CommPortIdentifier myIdentifier = myChooser.getSelectedIdentifier();
		LRX201Adapter adapter = new LRX201Adapter(myIdentifier, new LRX201AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {
				model.log(tagID, antennaID, rssi);
				view.readTag(tagID, antennaID, rssi);
			}
		});
		adapter.start(NUM_ANTENNAS);
		//adapter.setReceiverGain(true);
	}
}
