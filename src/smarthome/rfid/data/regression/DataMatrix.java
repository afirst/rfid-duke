package smarthome.rfid.data.regression;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import smarthome.rfid.data.RegressionAlgorithm;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.Vector;

public class DataMatrix extends ArrayList<double[]> {
	private TrainingPointList data;
	
	public DataMatrix(TrainingPointList data) {
		this.data = data;
	}
	
	/**
	 * Get x,y,z,s1,s2,s3,s4,s5 vectors
	 * @param data
	 * @return
	 */
	public void loadVectors() {
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
		this.add(x); this.add(y); this.add(z); 
		this.add(s1); this.add(s2); this.add(s3); this.add(s4); this.add(s5);
	}
	
	public double[] getX() {
		return this.get(0);
	}
	
	public double[] getY() {
		return this.get(1);
	}
	
	public double[] getZ() {
		return this.get(2);
	}
	
	public double[] getS1() {
		return this.get(3);
	}
	
	public double[] getS2() {
		return this.get(4);
	}
	
	public double[] getS3() {
		return this.get(5);
	}
	
	public double[] getS4() {
		return this.get(6);
	}
	
	public double[] getS5() {
		return this.get(7);
	}
	
	public void print(double[] array) {
		for (int i=0; i<array.length; i++) {
			System.out.print(array[i]+",");
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		TrainingPointList data = new TrainingPointList(); 
		data.load("data.txt");
		DataMatrix matrix = new DataMatrix(data);
		matrix.loadVectors();
		matrix.print(matrix.getS5());
		System.out.println();
		System.out.println("size "+matrix.getY().length);
	}
}
