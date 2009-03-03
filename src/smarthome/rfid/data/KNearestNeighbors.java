package smarthome.rfid.data;
import java.io.*;
import java.util.*;

public class KNearestNeighbors {
	public static String CONTROL_ADDRESS = "data_files\\test1\\control.txt";
	
	public double[] returnNearestPoint(String testPoint, int k) {
		ArrayList<String[]> controlData = prepareControlData(CONTROL_ADDRESS);
		ArrayList<Double> signalDistancesList = makeSignalDistancesList(testPoint, controlData);
		return nearestPoint(controlData, signalDistancesList, k);
	}
	/**
	 * 
	 * @param s1 - A scanner object which contains contents of the 
	 * control/training data. 
	 * @return list of arrays, in which arrays contain coordinates and signals for controls
	 */
	public ArrayList<String[]> prepareControlData(String address) {
		Scanner s1 = makeScanner(ReadFile(address));
		ArrayList<String[]> controlData = new ArrayList<String[]>();
		while (s1.hasNextLine()) {
			String[] controlArray = s1.nextLine().split("\t");
			controlData.add(controlArray);
		}
		return controlData;
	}
	
	/**
	 * Return the distances from control data for each test point
	 * @param testLine - each test point
	 * @param controlList
	 * @return an ArrayList of doubles that are the distances to each control point
	 * Its length of the number of control points. 
	 */
	public ArrayList<Double> makeSignalDistancesList(String testLine, ArrayList<String[]> controlList) {
		String[] testArray = stringToArray(testLine);
		ArrayList<Double> list = new ArrayList<Double>();
		for (String[] controlArray : controlList) {
			Double dist = computeSignalDistance(controlArray, testArray);
			list.add(dist);
		}
		return list;
	}
	
	public double[] nearestPoint(ArrayList<String[]> controlData, ArrayList<Double> signalDistancesList, int k) {
		ArrayList<Integer> kshortest = findKMinIndices(signalDistancesList,k);
		double sum_x=0.0; double sum_y=0.0; double sum_z=0.0;
		for(int i=0; i<kshortest.size();i++) {
			String[] controlSignal = controlData.get(kshortest.get(i).intValue());
			sum_x = sum_x + Double.parseDouble(controlSignal[0]);
			sum_y = sum_y + Double.parseDouble(controlSignal[1]);
			sum_z = sum_z + Double.parseDouble(controlSignal[2]);
		}
		double[] ret = {sum_x/k, sum_y/k, sum_z/k};
		return ret;
	}
	
	public File ReadFile(String address) {
		return new File(address);
	}
	
	public Scanner makeScanner(File f) {
		Scanner s = null;
		try {
			s= new Scanner(f);
		} catch(FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
		return s;
	}
	
	/**
	 * 
	 * @param controlList - control signal in the form of a String array 
	 * [x, y, z orientation, s1, s2, s3, s4]
	 * @param testList = test signal
	 * @return distance between the two signals
	 */
	public Double computeSignalDistance(String[] controlList, String[] testList) {
		//signals is a 4-integer array
		double distance = 0;
		for (int i = 4; i<controlList.length; i++) {
			distance = distance + Math.pow(Double.parseDouble(controlList[i])-Double.parseDouble(testList[i]), 2);
		}
		return new Double(Math.sqrt(distance));
	}
	
	public String[] stringToArray(String testLine) {
		return testLine.split("\t");
	}
	
	/**
	 * 
	 * @param list
	 * @param k
	 * @return the indices of the k smallest elements of the list with the smallest distances
	 */
	public ArrayList<Integer> findKMinIndices(ArrayList<Double> inputList, int k) {
		HashMap<Double, Integer> map = new HashMap<Double, Integer>();
		for (int i=0; i<inputList.size(); i++) {
			map.put(inputList.get(i),new Integer(i));
		}
		//make a copy of list
		Collections.sort(inputList);
		ArrayList<Integer> ret = new ArrayList<Integer>(); //contains indices
		for (int i=0; i<k; i++) {
			ret.add(map.get(inputList.get(i)));
		}
		return ret; 
	}

}
