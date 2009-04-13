package smarthome.rfid.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TrainingPointList extends ArrayList<TrainingPoint> {	
	private static final long serialVersionUID = 1L;

	public void load(String filename) throws FileNotFoundException {
		File f = new File(filename);
		Scanner s = new Scanner(f);
		
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] l = line.split(",");
			double x = Double.parseDouble(l[0]);
			double y = Double.parseDouble(l[1]);
			double z= Double.parseDouble(l[2]);
			double o = Double.parseDouble(l[3]);			
			
			double[] signalStrengths = new double[l.length - 4];
			for (int i = 0; i < signalStrengths.length; i++) {
				signalStrengths[i] = Double.parseDouble(l[4 + i]);
			}
			
			this.add(new TrainingPoint(new Location(x, y, z), o, new SignalStrength(signalStrengths)));
		
		}
		s.close();
	}
	
	public void save(String filename) throws FileNotFoundException {
		File f = new File(filename);
		PrintWriter out = new PrintWriter(f);
		RoomMap roomMap = new RoomMap();
		roomMap.load("room-map.txt");
		for (TrainingPoint pt : this) {
			out.print(pt.location().x());
			out.print("\t");
			out.print(pt.location().y());
			out.print("\t");
			out.print(pt.location().z());
			out.print("\t");
			out.print(pt.orientation());
			if (pt.signalStrength().variance()!=-1) {
				U.Print("wtf");
				out.print("\t");
				out.print(pt.signalStrength().variance());
			}
			for (int i = 0; i < pt.signalStrength().size(); i++) {
				out.print("\t");
				out.print(pt.signalStrength().get(i));
			}
			out.print("\t");
			out.print(roomMap.getRoom(pt.location()));
			out.println();
		}		
		out.close();
	}
	
}
