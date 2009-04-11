package smarthome.rfid.data;

import java.util.ArrayList;

import smarthome.rfid.data.regression.Function;
import smarthome.rfid.data.regression.FunctionList;

public class RegressionAlgorithm implements Algorithm{
	private FunctionList list; 
	private TrainingPointList data;
	
	public RegressionAlgorithm(int order) {
		list = new FunctionList(data, order);
		list.loadAllFunctions(order);
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
			ret += estimates.get(i) * (signals.get(i) / sum); 
		}
		return ret;
	}
	
	public double evaluate(Function function, double x) {
		double value = 0; 
		for (int i=0; i<function.size(); i++) {
			value += Math.pow(x,i) * function.get(i);
		}
		return value;
	}
	
	@Override
	public void setTrainingData(TrainingPointList trainingData) {
		this.data = trainingData; 
	}	
}
