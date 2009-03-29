/*
package smarthome.rfid.data.analyzer;

import java.awt.Point;
import java.io.*;
import java.util.*;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;

public class FileParser {
	public static void main(String[] args) throws Exception {
		
	}
	
	public void parseFileByOrientation(String inFile, String outFile) throws FileNotFoundException {
		File f = new File(inFile);
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
		for (int i =0; i < rssi.size(); i+=12) {			
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
		
		File f1 = new File(outFile);
		PrintWriter out = new PrintWriter(f1);
		for (int i = 0; i < rssi1.size(); i++) {
			out.println(rssi1.get(i));
		}
		out.close();
	}
	
	@SuppressWarnings("unchecked")
	public void parseFileIntoTrainingPoints(String inFile, String outFile) throws FileNotFoundException {
		File f = new File(inFile);
		Scanner s = new Scanner(f);
		
		List<RSSI1> rssi = new ArrayList<RSSI1>();
			
		//Array has to be Sorted by Location (x, y)
		
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
		
		Point counter = new Point(rssi.get(0).x, rssi.get(0).y); 
		Map <Integer, ArrayList<Integer>> tempMap = null; 
		TrainingPointList trainingPointList = new TrainingPointList(); 
		
		for (int i = 0; i < rssi.size(); i++) {
			RSSI1 temp = rssi.get(i);
			Point tempPoint = new Point (temp.x, temp.y); 
			if (counter.equals(tempPoint)){
				//Do Nothing
			}
			else {
				counter = tempPoint; 
				Iterator it = tempMap.entrySet().iterator();
				
				while (it.hasNext()){
					Map.Entry pairs = (Map.Entry)it.next();
					
					double orientation = (Integer) pairs.getKey();
					ArrayList<Integer> signalStrengths = (ArrayList<Integer>) pairs.getValue();
					
					double[] vector = new double[signalStrengths.size()];

					for (int j = 0; i < signalStrengths.size(); j++) {
						vector[j] = (double) signalStrengths.get(j);
					}

					TrainingPoint trainingPoint = new TrainingPoint(new Location(temp.x, temp.y, temp.z), orientation, new smarthome.rfid.data.Vector(vector));
					trainingPointList.add(trainingPoint);
				}
				tempMap.clear(); 
			}
			
			if (tempMap.get(temp.o) == null) {
				tempMap.put(temp.o, new ArrayList<Integer>());
			} 
			tempMap.get(temp.o).add((temp.antenna-1), temp.signalStrength);
		}
		
		File f1 = new File(outFile);
		PrintWriter out = new PrintWriter(f1);
			
		for (int i = 0; i < trainingPointList.size(); i++) {
			out.println(trainingPointList.get(i));
		}
		out.close(); 
	}
		
	static class RSSI1 {
		public int x,y,z,o,antenna,signalStrength;
		public RSSI1(int x, int y, int z, int o, int a, int s){this.x=x;this.y=y;this.z=z;this.o=o;this.antenna=a;this.signalStrength=s;}
		public String toString() {return x+","+y+","+z+","+o+","+antenna+","+signalStrength;}
	}
}
*/