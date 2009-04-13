package smarthome.rfid.data;

import smarthome.rfid.data.filters.TrainingPointMerger;

public class FilteredKNN extends KNearestNeighbor {

	private static final int QUEUE_SIZE = 5;
	
	private SignalStrengthMovingAverages ssma;
	
	public FilteredKNN(int k) {
		super(k);
		ssma = new SignalStrengthMovingAverages(QUEUE_SIZE);
	}
	
	@Override
	public Location getLocation(int tagId, Vector signalStrength) {
		ssma.push(tagId, new SignalStrength(signalStrength));
		signalStrength = ssma.value(tagId);
		return super.getLocation(tagId, signalStrength);
	}

	@Override
	public void setTrainingData(TrainingPointList trainingData) {
		trainingData = (new TrainingPointMerger()).filter(trainingData);
		super.setTrainingData(trainingData);
	}
}
