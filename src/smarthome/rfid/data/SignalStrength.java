package smarthome.rfid.data;

public class SignalStrength extends Vector {
	private double v = -1;
	
	public SignalStrength(double[] data) {
		super(data);
	}
	
	public SignalStrength(Vector data) {
		super(data.vector);
	}
	
	public SignalStrength(Vector data, double variance) {
		super(data.vector);
		v = variance;
	}
	
	public double variance() {
		return v;
	}
	
	@Override
	public boolean equals(Object obj) {
		SignalStrength ss = (SignalStrength)obj;
		return super.equals(obj) && v == ss.v;
	}
	
	@Override
	public int hashCode() {
		int h = super.hashCode();
		h += new Double(v).hashCode();
		return h;
	}
	
	public static void main(String[] args) {
		double[] data = {2,3,6,2};
		SignalStrength s= new SignalStrength(data);
		U.Print(s.variance()==-1);
	}
}
