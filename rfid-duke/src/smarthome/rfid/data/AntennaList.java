package smarthome.rfid.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AntennaList extends ArrayList<Antenna> {		

	private static final long serialVersionUID = 1L;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Antenna a : this) {
			sb.append(a.toString());
			sb.append("\n");
		}
		return sb.toString(); 
	}
	
	public void load(String filename) throws FileNotFoundException {
		File f = new File(filename);
		Scanner s = new Scanner(f);
		
		int i = 0;
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] l = line.split(",");
			double x = Double.parseDouble(l[0]);
			double y = Double.parseDouble(l[1]);
			double z = Double.parseDouble(l[2]);
			this.add(new Antenna(i++, new Location(x, y, z)));
		}
		s.close();
	}
	
	public void save(String filename) throws FileNotFoundException {
		File f = new File(filename);
		PrintWriter out = new PrintWriter(f);
		for (int i = 0; i < this.size(); i++) {
			out.println(this.get(i));
		}
		out.close();
	}
}
