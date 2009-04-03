package smarthome.rfid.data.filters;

import smarthome.rfid.data.TrainingPointList;

public interface TrainingPointFilter {
	public TrainingPointList filter(TrainingPointList points);
}
