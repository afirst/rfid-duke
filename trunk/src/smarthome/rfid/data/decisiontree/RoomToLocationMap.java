package smarthome.rfid.data.decisiontree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import smarthome.rfid.data.Rectangle;
import smarthome.rfid.data.Room;
import smarthome.rfid.data.Location;

public class RoomToLocationMap extends TreeMap<Room, Location> {
	private static final long serialVersionUID = 1L;

	public void load(String address) throws FileNotFoundException {
		File f= new File(address);
		Scanner s = new Scanner(f);
		
		while (s.hasNext()) {
			String line = s.nextLine();
			String[] l = line.split("\t");
			Room room = new Room(l[0]);
			double x = Double.parseDouble(l[1]);
			double y = Double.parseDouble(l[2]);
			double z = Double.parseDouble(l[3]);
			this.put(room,new Location(x,y,z));
		}
		s.close();
	}
	
	public Location getLocationOffRoom(Room room) {
		return this.get(room);
	}
}
