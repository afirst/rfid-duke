package smarthome.rfid.data.filters;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.RFIDMath;
import smarthome.rfid.data.SignalStrength;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.Vector;

public class TrainingPointMerger implements TrainingPointFilter {
	
	public TrainingPointList filter(TrainingPointList points) {
		Map<Location, List<TrainingPoint>> map = new HashMap<Location, List<TrainingPoint>>();		
		for (TrainingPoint pt : points) {
			if (!map.containsKey(pt.location())) {
				map.put(pt.location(), new ArrayList<TrainingPoint>());
			}
			map.get(pt.location()).add(pt);
		}
		int dim = points.get(0).signalStrength().size();
		TrainingPointList newList = new TrainingPointList();
		for (Location loc : map.keySet()) {
			List<TrainingPoint> pts = map.get(loc);
			List<SignalStrength> ss = new ArrayList<SignalStrength>();
			for (TrainingPoint pt : pts) {
				ss.add(pt.signalStrength());
			}
			SignalStrength merged = SignalStrengthMerger.filter(ss);
			if (merged == null) merged = new SignalStrength(new double[dim]);
			TrainingPoint newPt = new TrainingPoint(loc, -1, merged);
			newList.add(newPt);
		}
		return newList;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		TrainingPointList list = new TrainingPointList();
		list.load("data.txt");
		TrainingPointMerger merger = new TrainingPointMerger();
		list = merger.filter(list);
		list.save("mergerOutput.txt");
	}
	
}
