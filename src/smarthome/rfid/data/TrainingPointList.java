package smarthome.rfid.data;

import java.io.File;
import java.io.FileNotFoundException;
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
				signalStrengths[i] = Double.parseDouble(l[3 + i]);
			}
			
			this.add(new TrainingPoint(new Location(x, y, z), o, new Vector(signalStrengths)));
		
		}
		s.close();
	}
}
