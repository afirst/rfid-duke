package smarthome.rfid.data.knntest;

import java.io.FileNotFoundException;

import smarthome.rfid.data.KNearestNeighbor;
import smarthome.rfid.data.Location;
import smarthome.rfid.data.Room;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.U;
import smarthome.rfid.data.Vector;

public class KNNTest {
	public static int K = 5;
	public static String ADDRESS = "data_files\\test3\\control.txt";
	public static String TEST_ADDRESS = "data_files\\test3\\test.txt";
	public static String SAVE_ADDRESS = "data_files\\test3\\rooms-for-test.txt";
	
	public void run() throws FileNotFoundException {
		KNearestNeighbor knn = new KNearestNeighbor(K);
		TrainingPointList data = new TrainingPointList();
		data.load(ADDRESS);
		knn.setTrainingData(data);
		double[] vector = {105,122,93,81,82};
		Vector signalStrength = new Vector(vector);
		Location loc = knn.getLocation(3, signalStrength);
		U.Print(loc);
		U.Print("wtf");
		U.Print(loc.getRoom());
	}
	
	public void getAllRooms() throws FileNotFoundException {
		TrainingPointList data = new TrainingPointList();
		data.load(ADDRESS);
		data.save("All the Rooms.txt");
		U.Print("done!");
	}
	
	public void getKNNResults() throws FileNotFoundException {
		KNearestNeighbor knn = new KNearestNeighbor(K);
		TrainingPointList data = new TrainingPointList();
		data.load(ADDRESS);
		knn.setTrainingData(data);
		TrainingPointList test = new TrainingPointList();
		test.load(TEST_ADDRESS);
		test.save(SAVE_ADDRESS);
		U.Print("Results......");
		for(int i=0; i<test.size();i++) {
			Vector signalStrength = (Vector)test.get(i).signalStrength();
			Location loc = knn.getLocation(3, signalStrength);
			printTestPoint(loc, loc.getRoom());
		}
		U.Print("done!");
	}
	
	public static void printTestPoint(Location loc, Room r) {
		System.out.println(""+loc.x()+"\t"+loc.y()+"\t"+loc.z()+"\t" + r);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		KNNTest test = new KNNTest();
		test.getKNNResults();
	}

	
}
