package smarthome.rfid.data.regression;

import java.util.ArrayList;

import smarthome.rfid.data.TrainingPointList;

public class FunctionList {
	DataMatrix matrix; 
	//these functions are the regression functions for s1, s2, s3, s4, s5
	ArrayList<Function> xFunctions;
	ArrayList<Function> yFunctions;
	ArrayList<Function> zFunctions;
	
	public FunctionList(TrainingPointList data, int order) {
		matrix = new DataMatrix(data); 
		matrix.loadVectors();
		xFunctions = new ArrayList<Function>();
		yFunctions = new ArrayList<Function>();
		zFunctions = new ArrayList<Function>();
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
	
	public void loadAllFunctions(int order) {
		for(int i=3; i<=7; i++) {
			Function x = getFunction(matrix.get(i), matrix.getX(), order);
			Function y = getFunction(matrix.get(i), matrix.getY(), order);
			Function z = getFunction(matrix.get(i), matrix.getZ(), order);
			xFunctions.add(x);
			yFunctions.add(y);
			zFunctions.add(z);
		}
	}
	
	public Function getFunction(double[] y, double[] x, int order) {
		double[][] data = convert(y,x);
		double[] coef = Regression.linear_equation(data,order);
		return new Function(coef);
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
