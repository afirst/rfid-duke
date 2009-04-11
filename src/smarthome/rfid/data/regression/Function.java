package smarthome.rfid.data.regression;

public class Function {
	//a function is basically an array of coefficients starting from x^0
	private double[] function; 
	
	public Function(int size) {
		function = new double[size];
	}
	
	public Function(double[] data) {
		function = data;
	}
	
	public int size() {
		return function.length;
	}
	
	public double get(int index) {
		return function[index];
	}
}
