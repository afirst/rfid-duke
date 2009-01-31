package dataCollector;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import dataCollector.FileParser.RSSI1;

import math.Antenna;


public class AntennaList {
	
	ArrayList <Antenna> antennaLocations;
	
	public AntennaList () {
		antennaLocations = new ArrayList<Antenna>();
	}
	
	public void addLocation(Antenna antenna) {
		antennaLocations.add(antenna);
	}
	
	public Iterator getIterator() {
		return antennaLocations.iterator();
	}
	
	public String toString() {
		String string = antennaLocations.get(0).x+","+antennaLocations.get(0).y+","+antennaLocations.get(0).f; 
		for (int i =1; i < antennaLocations.size(); i++) {
			string = string + "\n"+antennaLocations.get(i).x+","+antennaLocations.get(i).y+","+antennaLocations.get(i).f;
		}
		return string; 
	}
	
	public void loadFromFile(String filename) throws FileNotFoundException {
		File f = new File(filename);
		Scanner s = new Scanner(f);
		
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] l = line.split(",");
			int x = Integer.parseInt(l[0]);
			int y = Integer.parseInt(l[1]);
			int floor = Integer.parseInt(l[2]);
			antennaLocations.add(new Antenna(x, y, floor));
		}
		s.close();
	}
	
	public void saveToFile(String filename) throws FileNotFoundException {
		File f = new File(filename);
		PrintWriter out = new PrintWriter(f);
		for (int i = 0; i < antennaLocations.size(); i++) {
			out.println(antennaLocations.get(i));
		}
		out.close();
	}
}
