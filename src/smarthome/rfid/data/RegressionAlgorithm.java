package smarthome.rfid.data;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import smarthome.rfid.data.regression.Function;
import smarthome.rfid.data.regression.FunctionList;

public class RegressionAlgorithm implements Algorithm{
	private FunctionList list; 
	private TrainingPointList data;
	private int order;
	
	public RegressionAlgorithm(int order) {
		this.order = order;	
	}

	public Location getLocation(int tagId, Vector signalStrength) {
		ArrayList<Double> xEstimates = new ArrayList<Double>();
		ArrayList<Double> yEstimates = new ArrayList<Double>();
		ArrayList<Double> zEstimates = new ArrayList<Double>();
		for (int i=0; i<list.getXFunctions().size(); i++) {
			double xHat = evaluate(list.getXFunctions().get(i), signalStrength.get(i));
			double yHat = evaluate(list.getYFunctions().get(i), signalStrength.get(i));
			double zHat = evaluate(list.getZFunctions().get(i), signalStrength.get(i));
			xEstimates.add(xHat);
			yEstimates.add(yHat);
			zEstimates.add(zHat);
		}
		return new Location(weighted(signalStrength, xEstimates), weighted(
				signalStrength, yEstimates), weighted(signalStrength,
				zEstimates));
	}
	
	public Double getAverage(ArrayList<Double> list) {
		double sum = 0; 
		for(int i=0; i<list.size(); i++) {
			sum += list.get(i);
		}
		return sum / list.size();
	}
	
	/**
	 * Weighted by signals
	 * @param signals
	 * @param estimates
	 * @return
	 */
	public Double weighted(Vector signals, ArrayList<Double> estimates) {
		double sum = 0; 
		for(int i=0; i<signals.size(); i++) {
			sum += signals.get(i);
		}
		double ret = 0; 
		for (int i=0; i<estimates.size(); i++) {
			double weight = 0;
			if (signals.get(i)>=60 && signals.get(i)<=70) {
				//give it less weight
				weight = signals.get(i)/ (2*sum);
			}
			else {
				weight = 2 * signals.get(i) / sum;
			}
			ret += estimates.get(i) * weight; 
		}
		return ret;
	}
	
	/**
	 * Evaluate a function for a x
	 * @param function
	 * @param x
	 * @return
	 */
	public double evaluate(Function function, double x) {
		System.out.println(function);
		double value = 0; 
		for (int i=0; i<function.size(); i++) {
			value += Math.pow(x,i) * function.get(i);
		}
		return value;
	}
	
	@Override
	public void setTrainingData(TrainingPointList trainingData) {
		this.data = trainingData;
		list = new FunctionList(data, order);
		list.loadAllFunctions();
	}	
	
	public static void main(String[] args) throws FileNotFoundException {
		RegressionAlgorithm ra = new RegressionAlgorithm(1);
		TrainingPointList data = new TrainingPointList(); 
		data.load("data.txt");
		ra.setTrainingData(data);
		double[] ss = {0,	70,	65,	90,	68};
		Vector signalStrength = new Vector(ss);
		System.out.println(ra.getLocation(5, signalStrength));
	}
}
