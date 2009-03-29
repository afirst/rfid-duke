package smarthome.rfid.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class KNNExperiments {
	public static String TEST_ADDRESS = "data_files\\test1\\test.txt";
	public static String OUTPUT_ADDRESS = "data_files\\test1\\results.txt";
	
	/**
	 * Return the distances from control data for each test point
	 * 
	 * @param testScanner
	 * @return 
	 */
	public ArrayList<double[]> getAllHypothesizedPints(Scanner testScanner, ArrayList<String[]> controlList, int k) {
		ArrayList<double[]> all_points_list = new ArrayList<double[]>(); 
		KNearestNeighbors knn = new KNearestNeighbors();
		while (testScanner.hasNextLine()) {
			double[] hypothesized_point = knn.returnNearestPoint(testScanner.nextLine(), k);
			all_points_list.add(hypothesized_point);
		}
		return all_points_list;
	}
		
	public 
	
	public void main() {
		KNearestNeighbors knn = new KNearestNeighbors();
		Scanner s1 = knn.makeScanner(knn.ReadFile(KNearestNeighbors.CONTROL_ADDRESS));
		Scanner s2 = knn.makeScanner(knn.ReadFile(TEST_ADDRESS));
		int k = 1;
		PrintStream results = makeOutputStream(OUTPUT_ADDRESS);
		ArrayList<String[]> controlList = knn.prepareControlData(KNearestNeighbors.CONTROL_ADDRESS);

	}
	
	public PrintStream makeOutputStream(String address) {
		PrintStream results = null;
		try {
			results = new PrintStream(new FileOutputStream (address));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
		return results;
	}
	
	public static void main(String arg[]) {
		main();
	}

}
