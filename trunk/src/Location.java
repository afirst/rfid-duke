
public class Location {
	private int x;
	private int y;
	private int z;
	private int orientation;
	
	public Location(int x, int y, int z, int orientation) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.orientation=z;
	}
	
	public double[] toArray() {
		//convert into an array
		double[] ret = new double[4];
		ret[0]=x;
		ret[1]=y;
		ret[2]=z;
		ret[3]=orientation;
		return ret;
	}
}
