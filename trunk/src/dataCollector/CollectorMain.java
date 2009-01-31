package dataCollector;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;

import javax.comm.CommPortIdentifier;
import javax.swing.*;

import machineCode.Adapter;
import machineCode.AdapterListener;
import machineCode.PortChooser;

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
		Adapter adapter = new Adapter(myIdentifier, new AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {
				model.updateSignalStrength(tagID, rssi);
			}
		});
		adapter.enableAutoPolling();
		adapter.setReceiverGain(true);
	}
}
