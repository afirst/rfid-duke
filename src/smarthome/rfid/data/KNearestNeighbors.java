package smarthome.rfid.data;
import java.io.*;
import java.util.*;

public class KNearestNeighbors {
	/**
	 * 
	 * @param controlList - control signal
	 * @param testList = test signal
	 * @return distance between the two signals
	 */
	public Double computeDistance(String[] controlList, String[] testList) {
		//signals is a 4-integer array
		double distance = 0;
		for (int i = 4; i<controlList.length; i++) {
			distance = distance + Math.pow(Double.parseDouble(controlList[i])-Double.parseDouble(testList[i]), 2);
		}
		return new Double(distance);
	}
	
	/**
	 * 
	 * @param list
	 * @param k
	 * @return the indices of the k smallest elements of the list with the smallest distances
	 */
	public ArrayList<Integer> findKMin(ArrayList<Double> inputList, int k) {
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
	
	public static void main(String arg[]) {
		KNearestNeighbors knn = new KNearestNeighbors();
		File control = new File("data\\control.txt");
		File test = new File("data\\test.txt"); //these would be signals
		Scanner s1 = null;
		Scanner s2 = null;
		try {
			s1 = new Scanner(control);
			s2 = new Scanner(test);
		} catch(FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
		int k = 1;
		ArrayList<Double> list = new ArrayList<Double>(); //list of dists
		PrintStream results = null;
		try {
			results = new PrintStream(new FileOutputStream ("data\\results.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}	
		
		ArrayList<String[]> controlList = prepareControlList(s1);
		double x = 0;
		double y = 0;
		double z = 0;
		while (s2.hasNextLine()) {
			String testLine = s2.nextLine();
			String[] testArray = testLine.split("\t");
			for (String[] controlArray : controlList) {
				Double dist = knn.computeDistance(controlArray, testArray);
				list.add(dist);
			}
			ArrayList<Integer> kshortest = knn.findKMin(list,4);
			//System.out.println(kshortest.size());
			//System.out.println(controlList.size());
			for(int i=0; i<kshortest.size();i++) {
				String[] controlSignal = controlList.get(kshortest.get(i).intValue());
				x = x + Double.parseDouble(controlSignal[0]);
				y = y + Double.parseDouble(controlSignal[1]);
				z = z + Double.parseDouble(controlSignal[2]);
			}
			x = x/4.0;
			y = y/4.0;
			z = z/4.0;
			results.println(x + ", " + y + ", " + z);
			
		}
	}

	private static ArrayList<String[]> prepareControlList(Scanner s1) {
		ArrayList<String[]> controlList = new ArrayList<String[]>();
		while (s1.hasNextLine()) {
			String controlLine = s1.nextLine();
			String[] controlArray = controlLine.split("\t");
			controlList.add(controlArray);
		}
		return controlList;
	}
}
