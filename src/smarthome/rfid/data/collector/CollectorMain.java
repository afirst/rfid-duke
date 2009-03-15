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
	public static void main(String[] args) {				
		final CollectorModel model = new CollectorModel(); 
		CollectorView view = new CollectorView(model);
		//CollectorNetworkController net = new CollectorNetworkController(model);
		//net.start();
		
		PortChooser myChooser = new PortChooser(null); 
		myChooser.setVisible(true);// blocking wait
		CommPortIdentifier myIdentifier = myChooser.getSelectedIdentifier();
		LRX201Adapter adapter = new LRX201Adapter(myIdentifier, new LRX201AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {
				model.updateSignalStrength(tagID, rssi);
			}
		});
		adapter.start(5);
		adapter.setReceiverGain(true);
	}
}
