package smarthome.rfid.data.collector;
import java.util.*;

import smarthome.rfid.data.RSSIReading;



/*
 * CollectorModel is the state for the gui. Contains hashMap of
 * LocationTuple to DataTuple.
 * 
 */

public class CollectorModel {

	public TreeMap<Integer, RSSIReading> ssMap = new TreeMap<Integer, RSSIReading>();
	private ArrayList <DataTuple> myData = new ArrayList <DataTuple> (); 
	private int currentSignalStrength; 
	
	public CollectorModel () {
		myData.clear(); 
	}
	
	public synchronized boolean addData (DataTuple data) {
//		Iterator <DataTuple> iterator = myData.iterator(); 
		
		//while (iterator.hasNext()) {
//			if (data.equals(iterator.next())){
				//return false; 
			//}
		//}
		
		System.out.println("Data added");
		myData.add(data);
		return true; 
	}
	
	public synchronized void removeData (DataTuple data) {
		for (int i = 0; i < myData.size(); i++) {
			if (data.equals(myData.get(i))){
				myData.remove(i);
				break; 
			}
		}
	}
	
	public Iterator<DataTuple> iterator () {
		return myData.iterator();
	}
	
	public synchronized void clear () {
		myData.clear();
	}
	
	public synchronized void printMap () {
		Iterator <DataTuple> iterator = myData.iterator(); 
		
		while (iterator.hasNext()) {
			System.out.println(iterator.toString());
		}
	}
	
	public synchronized void loadFile (String file) {
		Iterator <String> iterator = FileIO.read(file);
		while (iterator.hasNext()) {
			DataTuple tempTuple = new DataTuple(iterator.next());
			myData.add(tempTuple);
		}
	}
	
	public synchronized void outputToFile (String file) {
		String [] data = new String [myData.size()];
		for (int i = 0; i < myData.size(); i++) {
			data[i] = myData.get(i).toString();
		}
		FileIO.write(file, data);
	}
	
	public int getSize() {
		return myData.size(); 
	}
	
	public void updateSignalStrength(int tagId, int signalstrength) {
		ssMap.put(tagId, new RSSIReading(signalstrength));
	}
	
	public int getRSSI(int tagId) {
		if (!ssMap.containsKey(tagId))
			return 0;
		RSSIReading r = ssMap.get(tagId);
		if (System.currentTimeMillis() - r.time() > Settings.POLL_INTERVAL) {
			return 0;
		}
		return r.rssi();
	}
}
