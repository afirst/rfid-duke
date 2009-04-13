package smarthome.rfid.data.analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.SignalStrength;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.Vector;

public class DataAnalyzer implements Iterable<TrainingPoint> {

	public static void main (String [] Args){
		try {
			DataAnalyzer analyzer = new DataAnalyzer ("data.txt");
			int k=0;
			for (TrainingPoint pt : analyzer) {
				k++;
				System.out.println(pt);
			}
			System.out.println(k);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public ArrayList <TrainingPoint> DataList = new ArrayList <TrainingPoint> ();  
	
	public DataAnalyzer (String Filename) throws FileNotFoundException{
		File f = new File(Filename);
		Scanner s = new Scanner(f);
		
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] l = line.split("\t");
			double x = Double.parseDouble(l[0]);
			double y = Double.parseDouble(l[1]);
			double z= Double.parseDouble(l[2]);
			double o = Double.parseDouble(l[3]);
			
			double[] signalStrengths = new double[l.length - 4];
			for (int i = 0; i < signalStrengths.length; i++) {
				signalStrengths[i] = processDouble(l[4 + i]);
			}
			
			DataList.add(new TrainingPoint(new Location(x, y, z), o, new SignalStrength(signalStrengths)));
		
		}
		s.close();
	}
	
	public double processDouble(String str) {
		double x = Double.parseDouble(str);
		return x>=60?x-60:x;
	}
	
	public Iterator<TrainingPoint> iterator() {
		return DataList.iterator();
	}
	
	public void printToFile(String file) throws FileNotFoundException {

		File f1 = new File(file);
		PrintWriter out = new PrintWriter(f1);
		for (int i = 0; i < DataList.size(); i++) {
			out.println(DataList.get(i));
		}
		out.close();
	}
	
}
