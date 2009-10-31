package smarthome.rfid.data;

import java.io.FileNotFoundException;

import smarthome.rfid.data.decisiontree.DecisionTree;
import smarthome.rfid.data.decisiontree.RoomToLocationMap;

public class DecisionTreeAlgorithm implements Algorithm {
	private DecisionTree tree;
	private RoomToLocationMap roomToLocationMap; 
	private TrainingPointList trainingData; 
	private static final String TREE_ADDRESS = "decisionTree.txt";
	private static final String ROOM_TO_LOCATION_ADDRESS = "room-to-location-map.txt";
	
	public DecisionTreeAlgorithm(){
		tree = new DecisionTree();
		roomToLocationMap = new RoomToLocationMap();
	}
	
	public void setTrainingData(TrainingPointList trainingData) {
		this.trainingData = trainingData;
		try {
			tree.load(TREE_ADDRESS);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			roomToLocationMap.load(ROOM_TO_LOCATION_ADDRESS);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Only works for specific data right now
	 */
	public Location getLocation(int tagId, Vector signalStrength) {
		//Only return room, maybe get Room's centroid?
		Room r = tree.getRoom(signalStrength);
		return roomToLocationMap.getLocationOffRoom(r);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DecisionTreeAlgorithm dta = new DecisionTreeAlgorithm();
		TrainingPointList data = new TrainingPointList();
		data.load("data.txt");
		dta.setTrainingData(data);
		double[] ss = {105,	98,	73,	69,	70};
		Vector signalStrength = new Vector(ss);
		System.out.println(dta.getLocation(2, signalStrength));
	}
}
