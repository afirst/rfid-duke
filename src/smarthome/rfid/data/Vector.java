package smarthome.rfid.data;

import java.util.Iterator;

public class Vector {
	protected double[] vector;
	
	public Vector(double[] data) {
		this.vector = data;
	}
	
	public double[] data() {
		return vector;
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
			if (vector[i] != v.vector[i]) return false;
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
}
