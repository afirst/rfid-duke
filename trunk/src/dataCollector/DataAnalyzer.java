package dataCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import dataCollector.FileParser.RSSI1;

import math.AdvancedDataTuple;


public class DataAnalyzer {

	public static void main (String [] Args){
		try {
			DataAnalyzer analyzer = new DataAnalyzer ("formatteddata.csv");
			Iterator it = analyzer.Iterator();
			while (it.hasNext()) {
				System.out.println(it.next()); 
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public ArrayList <AdvancedDataTuple> DataList = new ArrayList <AdvancedDataTuple> ();  
	
	public DataAnalyzer (String Filename) throws FileNotFoundException{
		File f = new File(Filename);
		Scanner s = new Scanner(f);
		
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] l = line.split(",");
			float x = Float.parseFloat(l[0]);
			float y = Float.parseFloat(l[1]);
			float floor = Float.parseFloat(l[2]);
			float o = Float.parseFloat(l[3]);
			float s1 = Float.parseFloat(l[4]);
			float s2 = Float.parseFloat(l[5]);
			float s3 = Float.parseFloat(l[6]);
			float s4 = Float.parseFloat(l[7]);
						
			int[] signalStrengths = {(int) s1, (int) s2, (int) s3,(int) s4};
			
			//X, Y, floor, orientation, readerNumber, tagNumber, signalstrength, time
				DataList.add(new AdvancedDataTuple((int)x, (int)y, (int)floor, (int) o, 0, 0, signalStrengths));
		
		}
		s.close();
	}
	
	public Iterator Iterator () {
		return DataList.iterator();
	}
	
	public void prinToFile(String file) throws FileNotFoundException {

		File f1 = new File(file);
		PrintWriter out = new PrintWriter(f1);
		for (int i = 0; i < DataList.size(); i++) {
			out.println(DataList.get(i));
		}
		out.close();
	}
	
}
