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
			double[] mean = new double[dim];
			double[] variance = new double[dim];
			double s2 = 0;
			for (int i = 0; i < dim; i++) {
				double[] vd = new double[pts.size()];
				int j = 0;
				for (TrainingPoint pt : pts) {
					vd[j++] = pt.signalStrength().get(i);
				}
				Vector v = new Vector(vd);
				v = RFIDMath.removeAll(v, 0);
				if (v.size() == 0) v = new Vector(new double[] {0});
				double u_ = RFIDMath.mean(v);
				double s2_ = RFIDMath.variance(v);
				mean[i] = u_;
				variance[i] = s2_;
				s2 += s2_;
			}			
			SignalStrength ss = new SignalStrength(new Vector(mean), s2);
			TrainingPoint newPt = new TrainingPoint(loc, -1, ss);
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
