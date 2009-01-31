package math;

import dataCollector.Location;

public class KNearestNeighbor implements Algorithm{

	AdvancedDataTuple[] trainingData; 
	Antenna[] antennaLocations; 
	
	public KNearestNeighbor() {
		
	}
	
	public KNearestNeighbor(AdvancedDataTuple[] tags, Antenna[] antennas) {
		this.trainingData = tags; 
		this.antennaLocations = antennas; 
	}
	
	public Location getLocation(int tagId, int[] signalStrength) {
		float weight = 0; 
		float smallest = (float) TupleMath.getWeight(signalStrength, trainingData[0].signalStrength);;
		int index = 0; 
		
		for (int i =1; i < Math.min(signalStrength.length, trainingData.length); i++) {
			weight = (float) TupleMath.getWeight(signalStrength, trainingData[i].signalStrength);
			if (weight <= smallest) {
				smallest = weight; 
				index = i; 
			}
		}
		
		Location location = new Location(trainingData[index].x, trainingData[index].y, trainingData[index].f); 
		
		return location;
	}

	public void setTrainingData(AdvancedDataTuple[] tags, Antenna[] antennas) {
		trainingData = tags; 
		antennaLocations = antennas; 
	}
	
	/*public static void main (String[] args) {
		int[] signals1 = {11, 11, 11, 11}; 
		AdvancedDataTuple tuple1 = new AdvancedDataTuple(1, 1, 1, 1, 1, 1, signals1);
		
		int[] signals2 = {22, 22, 22, 22}; 
		AdvancedDataTuple tuple2 = new AdvancedDataTuple(2, 2, 2, 2, 2, 2, signals2);
		
		int[] signals3 = {33, 33, 33, 33}; 
		AdvancedDataTuple tuple3 = new AdvancedDataTuple(3, 3, 3, 3, 3, 3, signals3);
		
		int[] signals4 = {44, 44, 44, 44}; 
		AdvancedDataTuple tuple4 = new AdvancedDataTuple(4, 4, 4, 4, 4, 4, signals4);
		
		int[] signals5 = {55, 55, 55, 55}; 
		AdvancedDataTuple tuple5 = new AdvancedDataTuple(5, 5, 5, 5, 5, 5, signals5);
	
		AdvancedDataTuple[] tuples = {tuple1, tuple2, tuple3, tuple4, tuple5};
		
		int[] testSignal = {25, 25, 25, 25};
		
		NearestNeighborAlgorithm newAlgorithm = new NearestNeighborAlgorithm(tuples, null);
	
		Location result = newAlgorithm.getLocation(1, testSignal);
		
		System.out.println(result.toString());
	}*/
} 


}
