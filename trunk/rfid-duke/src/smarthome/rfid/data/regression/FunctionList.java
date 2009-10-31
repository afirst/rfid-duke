package smarthome.rfid.data.regression;

import java.util.ArrayList;

import smarthome.rfid.data.TrainingPointList;

public class FunctionList {
	DataMatrix matrix; 
	//these functions are the regression functions for s1, s2, s3, s4, s5
	ArrayList<Function> xFunctions;
	ArrayList<Function> yFunctions;
	ArrayList<Function> zFunctions;
	int order; 
	
	public FunctionList(TrainingPointList data, int order) {
		matrix = new DataMatrix(data); 
		matrix.loadVectors();
		xFunctions = new ArrayList<Function>();
		yFunctions = new ArrayList<Function>();
		zFunctions = new ArrayList<Function>();
		this.order = order;
	}
	
	public ArrayList<Function> getXFunctions() {
		return xFunctions;
	}
	
	public ArrayList<Function> getYFunctions() {
		return yFunctions;
	}
	
	public ArrayList<Function> getZFunctions() {
		return zFunctions;
	}
	
	public void loadAllFunctions() {
		for(int i=3; i<=7; i++) {
			//We want to regress x, y, z on the signal strengths. 
			Function x = getFunction(matrix.getX(), matrix.get(i), order);
			Function y = getFunction(matrix.getY(), matrix.get(i), order);
			Function z = getFunction(matrix.getZ(), matrix.get(i), order);
			xFunctions.add(x);
			yFunctions.add(y);
			zFunctions.add(z);
		}
	}
	
	public Function getFunction(double[] y, double[] x, int order) {
		double[][] data = condenseAndConvert(y,x);
		double[] coef = Regression.linear_equation(data,order);
		return new Function(coef);
	}
	
	/**
	 * When regressing, we do not want to regress on x with 0s, so take off all 0s from x and its corresponding ys.
	 * @param y
	 * @param x
	 * @return
	 */
	public double[][] condenseAndConvert(double[] y, double[] x) {
		ArrayList<Double> yList = new ArrayList<Double>();
		ArrayList<Double> xList = new ArrayList<Double>();
		for (int i=0; i<y.length; i++) {
			if (x[i]>= 65) {
				yList.add(y[i]);
				xList.add(x[i]);
			}
		}
		double[][] ret = new double[yList.size()][2];
		for (int i=0; i<yList.size(); i++) {
			ret[i][0]=yList.get(i);
			ret[i][1]=xList.get(i);
		}
		return ret;
	}
	
	public double[][] convert(double[] y, double[] x) {
		double[][] data = new double[y.length][2];
		for (int i=0; i<y.length; i++) {
			data[i][0]=y[i];
			data[i][1]=x[i];
		}
		return data;
	}
	
	public static void main(String[] args) {
		double[] y = {3,5,2};
		double[] z = {1,6,100};
		TrainingPointList data = new TrainingPointList();
		FunctionList list = new FunctionList(data, 3);
		double[][] test = list.convert(y,z);
		for(int i=0; i<test.length;i++) {
			for (int j=0; j<test[0].length; j++) {
				System.out.println(""+i+","+j+": "+test[i][j]);
			}
		}
	}
}
