package dataCollector;

import java.io.*;
import java.util.*;

public class FileParser {
	public static void main(String[] args) throws Exception {
		File f = new File("data.csv");
		Scanner s = new Scanner(f);
		List<RSSI1> rssi = new ArrayList<RSSI1>();
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] l = line.split(",");
			int x = Integer.parseInt(l[0]);
			int y = Integer.parseInt(l[1]);
			int z = Integer.parseInt(l[2]);
			int o = Integer.parseInt(l[3]);
			int antenna = Integer.parseInt(l[4]);
			int signalStrength = Integer.parseInt(l[5]);
			rssi.add(new RSSI1(x,y,z,o,antenna,signalStrength));
		}
		s.close();
		
		List<RSSI1> rssi1 = new ArrayList<RSSI1>();
		for (int i =0; i < rssi.size();i+=12) {			
			int x = rssi.get(i).x;
			int y = rssi.get(i).y;
			int z = rssi.get(i).z;
			int a = rssi.get(i).antenna;			
			RSSI1 r0 = new RSSI1(x,y,z,0,a,(rssi.get(i).signalStrength+rssi.get(i+4).signalStrength+rssi.get(i+8).signalStrength)/3);
			RSSI1 r1 = new RSSI1(x,y,z,1,a,(rssi.get(i+1).signalStrength+rssi.get(i+5).signalStrength+rssi.get(i+9).signalStrength)/3);
			RSSI1 r2 = new RSSI1(x,y,z,2,a,(rssi.get(i+2).signalStrength+rssi.get(i+6).signalStrength+rssi.get(i+10).signalStrength)/3);
			RSSI1 r3 = new RSSI1(x,y,z,3,a,(rssi.get(i+3).signalStrength+rssi.get(i+7).signalStrength+rssi.get(i+11).signalStrength)/3);
			rssi1.add(r0);
			rssi1.add(r1);
			rssi1.add(r2);
			rssi1.add(r3);
		}
		
		File f1 = new File("data1.csv");
		PrintWriter out = new PrintWriter(f1);
		for (int i = 0; i < rssi1.size(); i++) {
			out.println(rssi1.get(i));
		}
		out.close();
	}
	
	static class RSSI1 {
		public int x,y,z,o,antenna,signalStrength;
		public RSSI1(int x, int y, int z, int o, int a, int s){this.x=x;this.y=y;this.z=z;this.o=o;this.antenna=a;this.signalStrength=s;}
		public String toString() {return x+","+y+","+z+","+o+","+antenna+","+signalStrength;}
	}
}
