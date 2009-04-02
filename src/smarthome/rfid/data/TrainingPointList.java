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
		for (TrainingPoint pt : this) {
			out.print(pt.location().x());
			out.print(",");
			out.print(pt.location().y());
			out.print(",");
			out.print(pt.location().z());
			out.print(",");
			out.print(pt.orientation());			
			for (int i = 0; i < pt.signalStrength().size(); i++) {
				out.print(",");
				out.print(pt.signalStrength().get(i));
			}
			out.println();
		}		
		out.close();
	}
}
