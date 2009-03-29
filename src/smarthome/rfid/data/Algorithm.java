package smarthome.rfid.data;

public interface Algorithm {
	void setTrainingData(TrainingPointList trainingData);
	Location getLocation(int tagId, Vector signalStrength);
}
