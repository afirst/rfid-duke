package smarthome.rfid.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Vector implements Iterable<Double> {
	protected Double[] vector;
	
	public Vector(int size) {
		this(new double[size]);
	}
	
	public Vector(double[] data) {
		Double[] arr = new Double[data.length];
		for (int i = 0; i < data.length; i++) {
			arr[i] = data[i];
		}
		this.vector = arr;
	}
		
	public Vector(Double[] data) {
		this.vector = data.clone();
	}
	
	public Vector(List<Double> data) {
		this.vector = data.toArray(new Double[0]);
	}
	
	public String toString (){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		boolean b = false;
		for (double x : vector) {			
			if (b) {
				sb.append(", ");
			}
			else {
				b = true;
			}
			sb.append(x);
		}
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector)obj;
		if (v.vector.length != vector.length) return false;
		for (int i = 0; i < vector.length; i++) {						
			if (Math.abs(vector[i] - v.vector[i]) > 0.0001) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int h = new Integer(vector.length).hashCode();
		for (int i = 0; i < vector.length; i++) {
			h += new Double(vector[i]).hashCode();
		}
		return h;
	}

	public int size() {
		return vector.length;
	}
	
	public double get(int index) {
		return vector[index];
	}
	
	public void add(Vector v) {
		if (v.size() != this.size()) {
			throw new IllegalArgumentException("Sizes don't match.");
		}
		for (int i = 0; i < size(); i++) {
			vector[i] += v.vector[i];
		}		
	}
	
	public void scale(double scaleFactor) {
		for (int i = 0; i < size(); i++) {
			vector[i] *= scaleFactor;
		}		
	}
	
	public Vector copy() {
		return new Vector(vector.clone());
	}

	public Iterator<Double> iterator() {
		return Arrays.asList(vector).iterator();
	}
}
