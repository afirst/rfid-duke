package smarthome.rfid.data;

public class KNearestNeighbor implements Algorithm{

	TrainingPointList trainingData; 
	AntennaList antennaLocations; 
	
	public KNearestNeighbor() {
		
	}
	
	public KNearestNeighbor(TrainingPointList trainingData, AntennaList antennas) {
		this.trainingData = trainingData; 
		this.antennaLocations = antennas; 
	}
	
	public Location getLocation(int tagId, Vector signalStrength) {
		double weight = 0; 
		double smallest = RFIDMath.getWeight(signalStrength.data(), trainingData.get(0).signalStrength().data());
		int index = 0; 
		
		for (int i =1; i < Math.min(signalStrength.size(), trainingData.size()); i++) {
			weight = RFIDMath.getWeight(signalStrength.data(), trainingData.get(i).signalStrength().data());
			if (weight <= smallest) {
				smallest = weight; 
				index = i; 
			}
		}
		
		Location location = trainingData.get(index).location(); 
		
		return location;
	}

	public void setTrainingData(TrainingPointList trainingData, AntennaList antennas) {
		this.trainingData = trainingData; 
		this.antennaLocations = antennas; 
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
