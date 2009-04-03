package smarthome.rfid.data.filters;

import java.util.ArrayList;
import java.util.List;

import smarthome.rfid.data.TrainingPointList;

public class TrainingPointFilterList implements TrainingPointFilter {
	private List<TrainingPointFilter> filters;
	
	public TrainingPointFilterList() {
		filters = new ArrayList<TrainingPointFilter>();
	}
	
	public void add(TrainingPointFilter filter) {
		filters.add(filter);	
	}
	
	public TrainingPointList filter(TrainingPointList points) {
		for (TrainingPointFilter f : filters) {
			points = f.filter(points);
		}
		return points;
	}
}
