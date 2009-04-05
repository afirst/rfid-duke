package smarthome.rfid.data;

import java.util.ArrayList;

public class RegressionAlgorithm implements Algorithm{
	private TrainingPointList data;
	
	/**
	 * Get x,y,z,s1,s2,s3,s4,s5 vectors
	 * @param data
	 * @return
	 */
	public ArrayList<double[]> getVectors(TrainingPointList data) {
		int dim = data.size();
		double[] x = new double[dim];
		double[] y = new double[dim];
		double[] z = new double[dim];
		double[] s1 = new double[dim];
		double[] s2 = new double[dim];
		double[] s3 = new double[dim];
		double[] s4 = new double[dim];
		double[] s5 = new double[dim];

		for (int i=0; i<data.size();i++) {
			TrainingPoint point = data.get(i);
			x[i] = point.location().x();
			y[i] = point.location().y();
			z[i] = point.location().z();
			Vector signal = point.signalStrength();
			s1[i] = signal.get(0);
			s2[i] = signal.get(1);
			s3[i] = signal.get(2);
			s4[i] = signal.get(3);
			s5[i] = signal.get(4);
		}
		ArrayList<double[]> list = new ArrayList<double[]>();
		list.add(x); list.add(y); list.add(z); 
		list.add(s1); list.add(s2); list.add(s3); list.add(s4); list.add(s5);
		return list;
	}
	
	
	
}
