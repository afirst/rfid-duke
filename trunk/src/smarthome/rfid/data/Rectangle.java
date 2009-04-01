package smarthome.rfid.data;

public class Rectangle extends Shape {
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double z;

	public Rectangle(double x1, double x2, double y1, double y2, double z) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.z = z;
	}

	public boolean contains(Location location) {
		return contains(location.x(), location.y(), location.z());
	}

	public boolean contains(double x, double y, double z) {
		return (this.x1<=x && x<=this.x2) 
				&& (this.y1<=y && y<=this.y2) 
				&& (this.z==z);
	}

	public String toString() {
		return "(Rectangle:"+x1+","+x2+","+y1+","+y2+","+z+")";
	}
	public static void main(String[] args) {
		Rectangle r = new Rectangle(.00390625,.030078125,.01650625,.184375,2);
		Location test = new Location(.03,.1,2);
		U.Print(r.contains(test));
	}
}
