package smarthome.rfid.data;

import smarthome.rfid.data.decisiontree.DecisionTree;

public class DecisionTreeAlgorithm implements Algorithm {
	private DecisionTree tree;
	
	public void setTrainingData(TrainingPointList trainingData) {
		
	}
	
	/**
	 * Only works for specific data right now
	 */
	public Location getLocation(int tagId, Vector signalStrength) {
		//Only return room, maybe get Room's centroid?
		Room r = tree.getRoom(1, signalStrength);
		return new Location();
	}
	
		
}
