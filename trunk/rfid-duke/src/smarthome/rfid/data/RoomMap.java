package smarthome.rfid.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class RoomMap extends HashMap<Rectangle, Room> {
	private static final long serialVersionUID = 1L;

	public void load(String address) throws FileNotFoundException {
		File f= new File(address);
		Scanner s = new Scanner(f);
		
		while (s.hasNext()) {
			String line = s.nextLine();
			String[] l = line.split("\t");
			double x1 = Double.parseDouble(l[0]);
			double x2 = Double.parseDouble(l[1]);
			double y1 = Double.parseDouble(l[2]);
			double y2 = Double.parseDouble(l[3]);
			double z = Double.parseDouble(l[4]);
			Room room = new Room(l[5]);
			this.put(new Rectangle(x1,x2,y1,y2,z),room);
		}
		s.close();
	}
	
	public Room getRoom(Location loc) {
		for(Rectangle r : this.keySet()) {
			if (r.contains(loc)) {
				return this.get(r);
			}
		}
		return Room.OUTSIDE;
	}

	public static void main(String[] args) throws FileNotFoundException {
		RoomMap map = new RoomMap();
		map.load("room-map.txt");
		System.out.println(map);
	
		Location loc = new Location(.4,.2,2);
		System.out.println(map.getRoom(loc));
	}
}
