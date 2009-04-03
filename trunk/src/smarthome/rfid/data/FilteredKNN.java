package smarthome.rfid.data;

import smarthome.rfid.data.filters.TrainingPointMerger;

public class FilteredKNN extends KNearestNeighbor {

	public FilteredKNN(int k) {
		super(k);
	}

	@Override
	public void setTrainingData(TrainingPointList trainingData) {
		trainingData = (new TrainingPointMerger()).filter(trainingData);
		super.setTrainingData(trainingData);
	}
}
