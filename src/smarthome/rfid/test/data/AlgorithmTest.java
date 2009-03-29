package smarthome.rfid.test.data;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smarthome.rfid.data.Algorithm;
import smarthome.rfid.data.KNearestNeighbor;
import smarthome.rfid.data.Location;
import smarthome.rfid.data.RFIDMath;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.Vector;

public class AlgorithmTest {
	
	private static final Algorithm ALGORITHM = new KNearestNeighbor(1);
	private static final String TRAINING_DATA_FILE = "data.txt";
	private static final double CONTROL_PROPORTION = 0.7;
	private static final double NUM_TESTS = 100;
	
	private Algorithm algorithm;
	private TrainingPointList trainingData;
	
	public AlgorithmTest() throws FileNotFoundException {
		
		double u = 0;
		double s = 0;
		for (int k = 0; k < NUM_TESTS; k++) {
		
		// split data into control and test data
		trainingData = new TrainingPointList();
		trainingData.load(TRAINING_DATA_FILE);
		Collections.shuffle(trainingData);
		int controlSize = (int)(trainingData.size() * CONTROL_PROPORTION);
		int testSize = trainingData.size() - controlSize;
		TrainingPointList control = new TrainingPointList();
		TrainingPointList test = new TrainingPointList();
		for (int i = 0; i < trainingData.size(); i++) {
			if (i < controlSize)
				control.add(trainingData.get(i));
			else
				test.add(trainingData.get(i));
		}
		
		// train algorithm
		algorithm = ALGORITHM;	
		algorithm.setTrainingData(control);
		
		// simulate test data
		double[] errors = new double[test.size()];
		for (int i = 0; i < test.size(); i++) {
			TrainingPoint pt = test.get(i);
			
			// get experimental location
			Location experimental = algorithm.getLocation(0, pt.signalStrength());
			
			// get actual location
			Location actual = pt.location();
			
			// compare
			errors[i] = RFIDMath.getDistance(experimental, actual);
		}
		
		// compute mean error
		Vector e = new Vector(errors);
		double mean = RFIDMath.mean(e);
		double stdev = RFIDMath.stdev(e);
		u += mean;
		s += stdev;
		}
		
		u /= NUM_TESTS;
		s /= NUM_TESTS;
		
		System.out.println("Mean: " + u);
		System.out.println("Stdev: " + s);
	}
	
	public void run() {
		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		AlgorithmTest test = new AlgorithmTest();
		test.run();
	}
}
