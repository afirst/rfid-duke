package smarthome.rfid.data;

public interface Algorithm {
	void setTrainingData(TrainingPointList trainingData, AntennaList antennas);
	Location getLocation(int tagId, Vector signalStrength);
}
