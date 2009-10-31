package smarthome.rfid.data;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class KNearestNeighbor implements Algorithm {
	private TrainingPointList trainingData; 
	private int k;
	
	public KNearestNeighbor(int k) {
		this.k = k;
	}
	
	Location prevLocation;
	
	public Location getLocation(int tagId, Vector signalStrength) {			
		TreeMap<Double, TrainingPoint> distances = createDistanceMap(signalStrength);
		Iterator<Entry<Double, TrainingPoint>> it = distances.entrySet().iterator();
		Location result = new Location();
		for (int i = 0; i < k; i++) {
			Entry<Double, TrainingPoint> current = it.next();
			result.add(current.getValue().location());			
		}
		result.scale(1.0 / k);
		prevLocation = result;
		return result;
	}
	
	public TreeMap<Double, TrainingPoint> createDistanceMap(Vector signalStrength) {
		TreeMap<Double, TrainingPoint> distances = new TreeMap<Double, TrainingPoint>(); 		
		for (TrainingPoint pt : trainingData) {
			double distance = RFIDMath.getDistance(pt.signalStrength(), signalStrength);
			if (prevLocation != null) {
				distance += RFIDMath.getDistance(pt.location(), prevLocation)*10;
			}
			if (pt.location().z() > 1) {
				distance = 10000000;
			}
			distances.put(distance, pt);
		}
		return distances;
	}		

	public void setTrainingData(TrainingPointList trainingData) {
		this.trainingData = trainingData; 
	}	
} 
