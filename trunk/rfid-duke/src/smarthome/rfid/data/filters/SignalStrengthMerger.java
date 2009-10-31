package smarthome.rfid.data.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.RFIDMath;
import smarthome.rfid.data.SignalStrength;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.Vector;

public class SignalStrengthMerger {
	public static SignalStrength filter(Iterable<SignalStrength> points) {
		Iterator<SignalStrength> it = points.iterator();
		if (!it.hasNext()) {
			return null;
		}
		SignalStrength first = it.next();
		int count = 0;
		for (SignalStrength pt : points) {
			count++;
		}
		int dim = first.size();
		double[] mean = new double[dim];
		double[] variance = new double[dim];
		double s2 = 0;
		for (int i = 0; i < dim; i++) {
			double[] vd = new double[count];
			int j = 0;
			for (SignalStrength pt : points) {
				vd[j++] = pt.get(i);
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
		return ss;
	}
}
